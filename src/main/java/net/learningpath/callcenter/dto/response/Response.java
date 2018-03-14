package net.learningpath.callcenter.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.learningpath.callcenter.dto.request.Call;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SuccessResponse.class, name = "SuccessResponse"),
        @JsonSubTypes.Type(value = ErrorResponse.class, name = "ErrorResponse")}
)
public interface Response {

    Call getCall();

    boolean isSuccess();

    default String getErrorType() {return null;}

    String getMessage();

    default DetailedError getDetails() {
        return null;
    }

}
