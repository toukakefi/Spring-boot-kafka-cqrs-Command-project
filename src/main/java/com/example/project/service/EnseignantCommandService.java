package com.example.project.service;

import com.example.project.entity.Enseignant;
import com.example.project.repository.EnseignantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnseignantCommandService {

    @Autowired
    private EnseignantRepo repo;

    @Autowired
    private KafkaTemplate<String, EnsEvent> kafkaTemplate;

    @Transactional
    public Enseignant createEns(EnsEvent ens) {
        try {
            Enseignant enseignant = ens.getEnseignant();
            Enseignant savedEnseignant = repo.save(enseignant);
            publishCreateEvent(savedEnseignant);
            System.out.println("EnsEvent sent successfully to Kafka topic.");
            return savedEnseignant;
        } catch (Exception e) {
            // Handle the exception, log it, and optionally rethrow it if needed
            // You may also consider throwing a more specific exception type based on your requirements
            throw new RuntimeException("Error occurred while creating Enseignant", e);
        }
    }

    public Enseignant updateEns(Long id, EnsEvent ens) {
        Enseignant existEns = repo.findById(id).orElse(null);
        if (existEns != null) {
            Enseignant newEns = ens.getEnseignant();
            existEns.setFirstname(newEns.getFirstname());
            existEns.setLastname(newEns.getLastname());
            existEns.setAge(newEns.getAge());
            Enseignant updatedEns = repo.save(existEns);
            publishUpdateEvent(updatedEns);
            return updatedEns;
        } else {
            // Handle the case where the entity with the given ID is not found.
            return null;
        }
    }

//    public void delete(Long id) {
//        repo.deleteById(id);
//        publishDeleteEvent(id);
//    }

    private void publishCreateEvent(Enseignant enseignant) {
        EnsEvent event = new EnsEvent("createEnseignant", enseignant);
        kafkaTemplate.send("enseignant-event-topic", event);
    }

    private void publishUpdateEvent(Enseignant enseignant) {
        EnsEvent event = new EnsEvent("updateEnseignant", enseignant);
        kafkaTemplate.send("enseignant-event-topic", event);
    }

//    private void publishDeleteEvent(Long id) {
//        String deleteEventMessage = "deleteEnseignant:" + id;
//        kafkaTemplate.send("enseignant-event-topic", deleteEventMessage);
//    }
}
