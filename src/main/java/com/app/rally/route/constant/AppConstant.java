package com.app.rally.route.constant;

import java.time.format.DateTimeFormatter;

public class AppConstant {

    public static final int JWT_TOKEN_MAX_AGE_IN_SECS=60;

    public static final String USER_NOT_FOUND="ERR01";
    public static final String USER_ALREADY_REGISTERED="ERR02";

    public static final String STATE_NOT_FOUND_BY_COUNTRY="ERR03";
    public static final String CITY_NOT_FOUND_BY_COUNTRY_AND_STATE="ERR04";
    public static final String CLUB_NOT_FOUND_BY_COUNTRY_AND_STATE_AND_CITY ="ERR05";
    public static final String USER_PROFILE_IMAGE_SAVE_ERROR = "ERR06";
    public static final String CLUB_NOT_FOUND = "ERR07";
    public static final String CLUB_IMAGE_SAVE_ERROR = "ERR08";
    public static final String CLUB_REVIEW_IS_ALREADY_GIVEN = "ERR014";


    public static final String FILE_UPLOAD_ERROR = "ERR09";
    public static final String FILE_NOT_FOUND_ERROR = "ERR010";
    public static final String PROFILE_IMAGE_NOT_FOUND="ERR011";
    public static final String CLUB_PARSING_ERROR="ERR012";
    public static final String RUNTIME_SERVICE_EXCEPTION="ERR013";
    public static final String GET_PROFILE_URI = "/api/v1/auth/profile/image/";
    public static final String GET_CLUB_IMAGE_URI = "/api/v1/auth/club/image/";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");



    //CLUB MAPPER
    public static final int CLUB_NAME=0;
    public static final int CLUB_SHORT_ADDRESS=1;
    public static final int CLUB_FULL_ADDRESS=2;
    public static final int CLUB_DESCRIPTION=3;
    public static final int CLUB_CONTACT=4;
    public static final int CLUB_EMAIL=5;
    public static final int CLUB_COUNTRY=6;
    public static final int CLUB_STATE=7;
    public static final int CLUB_CITY=8;
    public static final int CLUB_LONGITUDE=9;
    public static final int CLUB_LATITUDE=10;
    public static final int CLUB_IMAGE=11;


    //TOURNAMENT MAPPER
    public static final int TOURNAMENT_NAME=12;
    public static final int TOURNAMENT_TYPE=13;
    public static final int TOURNAMENT_DESCRIPTION=14;
    public static final int TOURNAMENT_DATE=15;
    public static final int TOURNAMENT_RESULT=16;


    public static final String SHEET = "sheet3";
}
