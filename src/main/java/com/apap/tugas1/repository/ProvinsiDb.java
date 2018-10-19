package com.apap.tugas1.repository;
import java.util.List;


import com.apap.tugas1.model.ProvinsiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProvinsiDb extends JpaRepository< ProvinsiModel , Long >{

}


