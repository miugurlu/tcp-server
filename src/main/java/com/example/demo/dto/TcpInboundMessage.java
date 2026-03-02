package com.example.demo.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TCP'den gelen her mesajın ortak taşıyıcısı. type'a göre payload farklı anlam taşır.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcpInboundMessage {

    private String type;

    private JsonNode payload;
}
