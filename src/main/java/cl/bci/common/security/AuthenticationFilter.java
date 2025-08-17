package cl.bci.common.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	
	private static final AntPathMatcher PATHS = new AntPathMatcher();
	private static final String[] EXCLUDE = {
      "/auth/login", "/h2-console/**",
      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**"
    };

	public AuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) {
	    if (HttpMethod.OPTIONS.matches(req.getMethod())) return true;
	    String uri = req.getRequestURI();
	    for (String p : EXCLUDE) {
	    	if (PATHS.match(p, uri)) {
	    		return true;
	    	}
	    }
	    return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("AuthenticationFilter.doFilterInternal");
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
		      
		    	final String token = getTokenFromRequest(request);
		        
		        if (jwtUtil.validateToken(token)) {
		          String username = jwtUtil.extractUsername(token);
		          List<String> roles = jwtUtil.extractRoles(token); // asegúrate de tener este método
		          var authorities = roles == null ? List.<SimpleGrantedAuthority>of()
		              : roles.stream()
		                  .filter(StringUtils::hasText)
		                  .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
		                  .map(SimpleGrantedAuthority::new)
		                  .collect(Collectors.toList());

		          var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
		          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		          SecurityContextHolder.getContext().setAuthentication(authentication);
		        }
		    }
		
		filterChain.doFilter(request, response);
		
	}
	
	private String getTokenFromRequest(HttpServletRequest request) {
		final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
			return authHeader.substring(7);
		}
		return null;
	}
}
