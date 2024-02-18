package com.example.project.service;

import com.example.project.entity.Enseignant;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnsEvent {
    private String eventType;
    private Enseignant enseignant;


}
