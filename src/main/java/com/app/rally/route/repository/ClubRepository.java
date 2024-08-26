package com.app.rally.route.repository;

import com.app.rally.route.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {


    @Query("select distinct(c.country) from Club c group by c.country")
  List<String> allCountryList();

    @Query("select distinct(c.state) from Club c where c.country=:country group by c.country,c.state")
     List<String> findStatesByCountry(String country);


    @Query("select distinct(c.city) from Club c where c.country=:country and c.state=:state group by c.country,c.state,c.city")
    List<String> findCityByState(String country,String state);

    @Query("from Club c where c.country=:country and c.state=:state and c.city=:city")
    List<Club> findAllClubByCountryAndStateAndCity(String country,String state,String city);

    Optional<Club> findByName(String name);
}
