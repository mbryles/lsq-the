package com.blastingconcept.lsq.the.ports.rest.csv;

import lombok.*;

@Data
@ToString
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Payload<T> {

    private String message;
    private T data;
}
