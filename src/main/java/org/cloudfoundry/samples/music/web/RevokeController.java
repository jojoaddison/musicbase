package org.cloudfoundry.samples.music.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.cloudfoundry.samples.music.services.CommunicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RevokeController {

    @Value("${ssoServiceUrl:placeholder}")
    private String ssoServiceUrl;

    @Value("${security.oauth2.client.clientId:client1}")
    private String clientId;
    
    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;
    
    private CommunicationService communicationService;
    
    public RevokeController(CommunicationService communicationService){
    	this.communicationService = communicationService;
    }
    
	@GetMapping(value = "/revoke/user/{userId}")
    public void revokeUser(HttpServletRequest request, @PathVariable String userId) throws IOException {

        // Decode the JWT and extract the jti-token-key
        // Send the jti-token-key for revocation at: /oauth/token/revoke/{tokenId}
        // /oauth/token/revoke/{tokenId}
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		
        if (auth != null) {

    		String authorization = request.getHeader("Authorization");
	   		 if (authorization != null && authorization.contains("Bearer")) {
		            String tokenId = authorization.substring("Bearer".length() + 1);
		    		String uaaURL = ssoServiceUrl.concat("/oauth/token/revoke/user/").concat(userId);    		
		    		Map<String, String> headers  = new HashMap<>();
		    		headers.put("Authorization", authorization);
		    		headers.put("Accept", "application/json");		    		
		    		Map<String, Object> result = communicationService.getRequest(uaaURL, headers);
		    		if(result.containsKey(String.valueOf(HttpStatus.OK))){
		    			tokenServices.revokeToken(tokenId);
		    		}
		    }    		
    		
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
    }

	@GetMapping(value = "/revoke/client/{clientId}")
    public void revokeClient(HttpServletRequest request, @PathVariable String clientId) throws IOException {

        // Decode the JWT and extract the jti-token-key
        // Send the jti-token-key for revocation at: /oauth/token/revoke/{tokenId}
        // /oauth/token/revoke/{tokenId}
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		
        if (auth != null) {

    		String authorization = request.getHeader("Authorization");
	   		 if (authorization != null && authorization.contains("Bearer")) {
		            String tokenId = authorization.substring("Bearer".length() + 1);
		    		String uaaURL = ssoServiceUrl.concat("/oauth/token/revoke/user/").concat(clientId);    		
		    		Map<String, String> headers  = new HashMap<>();
		    		headers.put("Authorization", authorization);
		    		headers.put("Accept", "application/json");		    		
		    		Map<String, Object> result = communicationService.getRequest(uaaURL, headers);
		    		if(result.containsKey(String.valueOf(HttpStatus.OK))){
		    			tokenServices.revokeToken(tokenId);
		    		}
		    }    		
    		
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
    }
}
