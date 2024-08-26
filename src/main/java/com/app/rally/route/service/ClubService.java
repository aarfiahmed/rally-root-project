package com.app.rally.route.service;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.ClubReview;
import com.app.rally.route.domain.ClubReviewRequest;
import com.app.rally.route.domain.ClubSearchCriteria;
import com.app.rally.route.entity.Club;
import com.app.rally.route.entity.User;
import com.app.rally.route.exception.CluBException;
import com.app.rally.route.exception.UserException;
import com.app.rally.route.repository.ClubRepository;
import com.app.rally.route.repository.UserRepository;
import com.app.rally.route.util.ClubDistanceCalculator;
import com.app.rally.route.util.ExcelUtility;
import com.app.rally.route.util.ServiceUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClubService {

    @Autowired
    private final ClubRepository clubRepository;

    @Autowired
    private final ExcelUtility excelUtility;

    @Autowired
    private final FileUploadService fileUploadService;

    @Autowired
    private final UserRepository userRepository;


    @Autowired
    private final JwtService jwtService;


    public List<String> getCountryList() {
        List<String> countryList =  clubRepository.allCountryList();
        log.info("all country list from DB {}", countryList);
        return countryList;
    }

    public List<String> getStateByCountry(String country) {
        List<String> stateList = clubRepository.findStatesByCountry(country);
        if(stateList.isEmpty()){
            log.info("state not found for country {}", country);
            throw new CluBException(AppConstant.STATE_NOT_FOUND_BY_COUNTRY,"state not found for country " + country);
        }
        log.info("all state list from DB {}", stateList);
        return stateList;
    }

    public List<String> getCityByState(String country, String state) {
        List<String> stateList =  clubRepository.findCityByState(country,state);
        if(stateList.isEmpty()){
            log.info("city not found for country {} and state {}", country,state);
            throw new CluBException(AppConstant.CITY_NOT_FOUND_BY_COUNTRY_AND_STATE,"city not found for country " + country+ " and state "+state);
        }
        log.info("all city list from DB {}", stateList);
        return stateList;
    }



    public List<Club> getClubDetails(ClubSearchCriteria clubSearchCriteria) {
        List<Club> clubDetails = clubRepository.findAllClubByCountryAndStateAndCity(clubSearchCriteria.getCountry(), clubSearchCriteria.getState(), clubSearchCriteria.getCity());
        log.info("Total club founds {}",clubDetails.size());
        if(clubDetails.isEmpty()){
          String errorMessage=  "club not found for country "+clubSearchCriteria.getCountry()+" state "+clubSearchCriteria.getState()+" city "+clubSearchCriteria.getCity();
            throw new CluBException(AppConstant.CLUB_NOT_FOUND_BY_COUNTRY_AND_STATE_AND_CITY,errorMessage);
        }
        clubDetails.forEach(this::setClubImageDetails);

        if(StringUtils.hasLength( clubSearchCriteria.getLongitude()) && StringUtils.hasLength( clubSearchCriteria.getLatitude())){
            ClubDistanceCalculator obj = new ClubDistanceCalculator(Double.parseDouble(clubSearchCriteria.getLatitude()), Double.parseDouble(clubSearchCriteria.getLongitude()));
            Map<Double,Club> sortedClub= new HashMap<>();
     clubDetails.forEach(club -> {

       sortedClub.put( obj.calculateDistance(Double.parseDouble(club.getLatitude()),Double.parseDouble(club.getLongitude())),club);
     });
            LinkedHashMap<Double, Club> collect = sortedClub.entrySet().stream().sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey())).collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1, LinkedHashMap::new));
            return List.copyOf(  collect.values());

        }
        return clubDetails;
    }


    private void setClubImageDetails(Club club){
        String clubImageName = fileUploadService.getClubImageName(club.getName());
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AppConstant.GET_CLUB_IMAGE_URI)
                .path(clubImageName)
                .toUriString();
club.setClubImage(clubImageName==null ?null:fileDownloadUri);

    }


    public void addClub(MultipartFile file){
        try {
            List<Club> clubDetails = excelUtility.getClubDetails(file.getInputStream());
            log.info("Number of clubs {} are going to save on DB",clubDetails.size());
            clubRepository.saveAll(clubDetails);
            log.info("all club info are saved into DB");
            fileUploadService.saveClubTemplate(file);
            log.info("going to save the club images ");
           excelUtility. saveClubImage(file.getInputStream(),clubDetails);
        } catch (IOException e) {
            log.info("error occurred while getting the input stream {}",e.getMessage());
            throw new CluBException(AppConstant.CLUB_PARSING_ERROR,"fail to parse Excel file: " + e.getMessage());
        }
    }

    public Resource getClubTemplate(){
       return fileUploadService.getClubTemplate();
    }

    public void addClubReview(ClubReviewRequest req, HttpServletRequest request) {
        Club club = clubRepository.findById(Long.parseLong(req.getClubId())).orElseThrow(() -> new CluBException(AppConstant.CLUB_NOT_FOUND ,"Club not found for id "+req.getClubId()));
        log.info("club found and adding the review");
        String jwt = ServiceUtils.getToken(request);
        final String userEmail =jwtService.extractUserName(jwt);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserException(AppConstant.USER_NOT_FOUND, "User not found " + userEmail));
        log.info("user exist into the db");
        boolean isReviewGiven = isReviewGivenByUser(club.getReviews(), user.getEmail());
        log.info("is review is given by user {}",isReviewGiven);
        if(isReviewGiven){
            throw new CluBException(AppConstant.CLUB_REVIEW_IS_ALREADY_GIVEN,"Club review is already given by user "+user.getEmail());
        }
        club.getReviews().add(getClubReview(req,user));
      log.info("complete review which are going to save {}",club.getReviews());
        clubRepository.save(club);
        log.info("club review saved on db");
    }

    private ClubReview getClubReview(ClubReviewRequest req, User user){
        ClubReview review = new ClubReview();
        review.setReviewerEmail(user.getEmail());
        review.setReviewerFirstName(user.getFirstname());
        review.setReview(req.getReview());
        return review;
    }

    private boolean isReviewGivenByUser(List<ClubReview> dbReviews, String userEmail){
    return Optional.ofNullable(dbReviews).stream().flatMap(Collection::stream).anyMatch(clubReview -> clubReview.getReviewerEmail().equalsIgnoreCase(userEmail));

    }

    public List<ClubReview> getClubReviews(String clubId) {
        Club club = clubRepository.findById(Long.parseLong(clubId)).orElseThrow(() -> new CluBException(AppConstant.CLUB_NOT_FOUND, "Club not found for " + clubId));
        log.info("club review are {}",club.getReviews());
        return club.getReviews();
    }
}
