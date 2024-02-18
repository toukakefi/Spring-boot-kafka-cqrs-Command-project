package com.example.project.repository;


import com.example.project.entity.Enseignant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepo extends JpaRepository<Enseignant,Long> {

}
