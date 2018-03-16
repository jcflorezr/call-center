package net.learningpath.callcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.learningpath.callcenter.config.TestRootContext;
import net.learningpath.callcenter.config.web.WebContext;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.failed.servererror.InternalServerErrorResponse;
import net.learningpath.callcenter.dto.response.success.SuccessResponse;
import net.learningpath.callcenter.employee.Operator;
import net.learningpath.callcenter.service.Dispatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestRootContext.class, WebContext.class})
@WebAppConfiguration
public class CallCenterControllerImplTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Value("${endpoint-url}")
    private String endpointUrl;

    @Autowired
    private Dispatcher dispatcherMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.reset(dispatcherMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void callAttendedSuccessfully() throws Exception {
        String clientName = "mock client";
        Call call = new Call(clientName, new Operator());
        Response expectedResponse = SuccessResponse.newResponse(call);

        when(dispatcherMock.dispatchCall(anyObject())).thenReturn(expectedResponse);

        // {"call":{"clientName":"mock client","attendedBy":{"greeting":"Hi!, how can I help you?"}},"success":true,"message":"Call was attended successfully !!!"}
        mockMvc.perform(post(endpointUrl, clientName)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.call.clientName", is(call.getClientName())))
                .andExpect(jsonPath("$.call.attendedBy.greeting", is("Hi!, how can I help you?")))
                .andExpect(jsonPath("$.message", is("Call was attended successfully !!!")));
    }

    @Test
    public void failedResponseAfterAttendingTheCall() throws Exception {
        String clientName = "mock client";
        Call call = new Call(clientName, new Operator());
        Response expectedResponse = InternalServerErrorResponse.newResponse(call, new Exception("mock exception"));

        when(dispatcherMock.dispatchCall(anyObject())).thenReturn(expectedResponse);

//        {
//            "call": {
//                "clientName": "mock client",
//                "attendedBy": {
//                    "greeting": "Hi!, how can I help you?"
//                }
//            },
//            "success": false,
//            "errorType": "java.lang.Exception",
//            "message": "mock exception",
//            "details":{
//                "errorCauseType":"java.lang.Exception",
//                "errorCauseMessage":"mock exception",
//                "stackTrace": [
//                    {
//                        "className":"net.learningpath.callcenter.controller.CallCenterControllerImplTest",
//                        "methodName":"failedResponseAfterAttendingTheCall",
//                        "lineNumber":113,
//                        "methodIsNative":false
//                    }
//                    ...
//                ]
//            }
//        }

        mockMvc.perform(post(endpointUrl, clientName)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.call.clientName", is(call.getClientName())))
                .andExpect(jsonPath("$.call.attendedBy.greeting", is("Hi!, how can I help you?")))
                .andExpect(jsonPath("$.errorType", is("java.lang.Exception")))
                .andExpect(jsonPath("$.message", is("mock exception")))
                .andExpect(jsonPath("$.details.errorCauseType", is("java.lang.Exception")))
                .andExpect(jsonPath("$.details.errorCauseMessage", is("mock exception")))
                .andExpect(jsonPath("$.details.stackTrace", instanceOf(ArrayList.class)))
                .andExpect(jsonPath("$.details.stackTrace[0]", hasKey("className")))
                .andExpect(jsonPath("$.details.stackTrace[0]", hasKey("methodName")))
                .andExpect(jsonPath("$.details.stackTrace[0]", hasKey("lineNumber")))
                .andExpect(jsonPath("$.details.stackTrace[0]", hasKey("methodIsNative")));
    }

}