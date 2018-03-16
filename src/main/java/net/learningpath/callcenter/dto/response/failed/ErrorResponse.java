package net.learningpath.callcenter.dto.response.failed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.failed.badrequest.BadRequestErrorResponse;
import net.learningpath.callcenter.dto.response.failed.servererror.InternalServerErrorResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InternalServerErrorResponse.class, name = "InternalServerErrorResponse"),
        @JsonSubTypes.Type(value = BadRequestErrorResponse.class, name = "BadRequestErrorResponse")})
public interface ErrorResponse extends Response {
}
