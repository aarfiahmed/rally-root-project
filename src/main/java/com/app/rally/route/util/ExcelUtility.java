package com.app.rally.route.util;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.entity.Club;
import com.app.rally.route.entity.Tournament;
import com.app.rally.route.exception.CluBException;
import com.app.rally.route.repository.ClubRepository;
import com.app.rally.route.service.FileUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
@AllArgsConstructor
public class ExcelUtility {

    @Autowired
    private final ClubRepository clubRepository;

    private final FileUploadService fileUploadService;


    public List<Club> getClubDetails(InputStream is) {
        try {
            Map<String, Club> clubs = new HashMap<>();
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(AppConstant.SHEET);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                String clubName = currentRow.getCell(0).getStringCellValue().toUpperCase().trim();
                Club club = clubs.get(clubName);
                if (club == null) {
                    clubs.put(clubName, parseClubDetails(currentRow));
                } else {
                    club.getTournaments().add(parseTournament(currentRow));
                }
            }

            workbook.close();

            return new ArrayList<>(clubs.values());
        } catch (Exception e) {
            log.error("error occurred while parsing club info",e);
            throw new CluBException(AppConstant.CLUB_PARSING_ERROR,"fail to parse Excel file: " + e.getMessage());
        }
    }


    public void saveClubImage(InputStream inputStream, List<Club> clubs){
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            List<? extends PictureData> pictures = workbook.getAllPictures();
            for(int i=0;i<pictures.size();i++){
               saveClubImage( pictures.get(i),clubs.get(i));
            }

        } catch (Exception e) {
            throw new CluBException(AppConstant.CLUB_IMAGE_SAVE_ERROR,"Error while saving club image "+e.getMessage());
        }


    }

    private void saveClubImage(PictureData pictureData, Club club) throws IOException {
        fileUploadService.saveClubImage(pictureData.getData(), club.getName());
    }

    private Club parseClubDetails(Row currentRow) {
        Club club = new Club();
        club.setName(currentRow.getCell(AppConstant.CLUB_NAME).getStringCellValue().toUpperCase().trim());
        club.setShortAddress(currentRow.getCell(AppConstant.CLUB_SHORT_ADDRESS).getStringCellValue());
        club.setFullAddress(currentRow.getCell(AppConstant.CLUB_FULL_ADDRESS).getStringCellValue());
        club.setDescription(currentRow.getCell(AppConstant.CLUB_DESCRIPTION).getStringCellValue());
        club.setContact(currentRow.getCell(AppConstant.CLUB_CONTACT).getStringCellValue());
        club.setEmail(currentRow.getCell(AppConstant.CLUB_EMAIL).getStringCellValue());
        club.setCountry(currentRow.getCell(AppConstant.CLUB_COUNTRY).getStringCellValue().toUpperCase().trim());
        club.setState(currentRow.getCell(AppConstant.CLUB_STATE).getStringCellValue().toUpperCase().trim());
        club.setCity(currentRow.getCell(AppConstant.CLUB_CITY).getStringCellValue().toUpperCase().trim());
        club.setLongitude(String.valueOf(currentRow.getCell(AppConstant.CLUB_LONGITUDE).getNumericCellValue()));
        club.setLatitude(String.valueOf(currentRow.getCell(AppConstant.CLUB_LATITUDE).getNumericCellValue()));

        ArrayList<Tournament> tournaments = new ArrayList<>();
        tournaments.add(parseTournament(currentRow));
        club.setTournaments(tournaments);
        return club;
    }

    private Tournament parseTournament(Row currentRow) {
        Tournament tournament = new Tournament();
        tournament.setTournamentName(currentRow.getCell(AppConstant.TOURNAMENT_NAME).getStringCellValue().toUpperCase().trim());
        tournament.setType(currentRow.getCell(AppConstant.TOURNAMENT_TYPE).getStringCellValue().trim());
        tournament.setTournamentDescription(currentRow.getCell(AppConstant.TOURNAMENT_DESCRIPTION).getStringCellValue());
        tournament.setTournamentDate(LocalDateTime.parse(String.valueOf(currentRow.getCell(AppConstant.TOURNAMENT_DATE).getLocalDateTimeCellValue()),AppConstant.DATE_TIME_FORMATTER));
        tournament.setResult(currentRow.getCell(AppConstant.TOURNAMENT_RESULT).getStringCellValue());
        return tournament;
    }
}
