package de.symeda.sormas.api.error;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorDetails {

  private Date date = new Date(System.currentTimeMillis());
  private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
  private String dateFormatted = formatter.format(date);

  private String message;

  private String entity;

  public String getMessage() {
    return message;
  }

  public String getEntity() {
    return entity;
  }

  public String getDateFormatted() {
    return dateFormatted;
  }

  public ErrorDetails( String message, String entity) {
    this.message = message;
    this.entity = entity;
  }

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode caseJson = mapper.convertValue(this, JsonNode.class);
    return caseJson.toString();
  }

}
