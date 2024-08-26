package com.app.rally.route.controller;

import com.app.rally.route.domain.*;
import com.app.rally.route.entity.Club;
import com.app.rally.route.service.ClubService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource/clubs")
@Slf4j
@AllArgsConstructor
public class ClubController {

    @Autowired
    private final ClubService clubService;

    @GetMapping()
    public Response<List<Club>> getClubs(@Valid ClubSearchCriteria clubSearchCriteria) {
        log.info("inside ClubController.getClubs {}", clubSearchCriteria);
        return new Result<>(clubService.getClubDetails(clubSearchCriteria));
    }

    @GetMapping("/country")
    public Response<List<String>> getClubCountry() {
        return new Result<>(clubService.getCountryList());
    }

    @PostMapping("/review")
    public Response<String> addClubReview(@RequestBody ClubReviewRequest clubReviewRequest, HttpServletRequest request){
        log.info("in addClubReview method and review is  {}",clubReviewRequest);
        clubService.addClubReview(clubReviewRequest,request);
        return  new Result<>( "success");

    }

    @GetMapping("/review/{clubId}")
    public Response<List<ClubReview>> getClubReview(@PathVariable String clubId){
        log.info("in getClubReview method and getting club review  {}",clubId);
        return  new Result<>( clubService.getClubReviews(clubId));

    }

    @GetMapping("/country/{country}")
    public Response<List<String>> getClubStates(@PathVariable String country) {
        log.info("inside ClubController.getClubCountry {} ", country);
        return new Result<>(clubService.getStateByCountry(country.toUpperCase().trim()));
    }

    @GetMapping("/country/{country}/state/{state}")
    public Response<List<String>> getClubCity(@PathVariable String country, @PathVariable String state) {
        log.info("inside ClubController.getClubCountry {} and state {}", country, state);
        return new Result<>(clubService.getCityByState(country.toUpperCase().trim(), state.toUpperCase().trim()));
    }





}
