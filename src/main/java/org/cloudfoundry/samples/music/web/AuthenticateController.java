package org.cloudfoundry.samples.music.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloudfoundry.samples.music.services.CommunicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {

    @Value("${ssoServiceUrl:placeholder}")
    private String ssoServiceUrl;

    @Value("${security.oauth2.client.clientId:client1}")
    private String clientId;
    
    private CommunicationService communicationService;
    
    public AuthenticateController(CommunicationService communicationService){
    	this.communicationService = communicationService;
    }
    
	@RequestMapping(value = "/logout", method = GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Decode the JWT and extract the jti-token-key
        // Send the jti-token-key for revocation at: /oauth/token/revoke/{tokenId}
        // /oauth/token/revoke/{tokenId}
        
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		
        if (auth != null) {

    		String userId = getDecodeToken(auth, "USER_ID"); // the userId from the token
            
    		String uaaURL = ssoServiceUrl.concat("/oauth/token/list/user/").concat(userId);
    		
    		Map<String, String> headers  = new HashMap<>();
    		headers.put("Authorization", getDecodeToken(auth, "BEARER_TOKEN"));
    		headers.put("Accept", "application/json");
    		
    		Map<String, Object> userTokens = communicationService.getRequest(uaaURL, headers);
    		userTokens.get("200").
    		
    		
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        return "done.";
    }
	
	private String getDecodeToken(Authentication auth, String type){
		//TODO: extract userId from authentication token
		switch(type){
			case USER_ID :
			
			break;
			case BEARER_TOKEN :
				
				break;
			
			default:
				
			
		}
		return "user-id";
	}
}
