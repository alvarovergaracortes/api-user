package cl.bci.user.infrastructure.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
		UUID id,
		String name,
		String email,
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime created,
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime modified,
	    
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	    LocalDateTime lastLogin,
	    
	    String token,
	    
	    @JsonProperty("isactive")
	    boolean isActive,
	    List<PhoneResponse> phoneReponse
) {}
