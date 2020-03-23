package com.phcworld.web.api.movie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/movies")
@Slf4j
public class MovieApiController {

	@GetMapping("/view")
	public String view() {
		
		return "/movie/movie";
	}
	
	@GetMapping(value = "", produces="application/json;charset=UTF-8")
	public @ResponseBody String search(HttpServletRequest request) {
		String title = request.getParameter("title");
		String pnum = request.getParameter("pnum");
		if(pnum == null || pnum == ""){
			pnum = "1";
		}
		int pageNUM = Integer.parseInt(pnum);
		int start = (pageNUM-1)*10+1;
		
		String clientId = "id";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "secret";// 애플리케이션 클라이언트 시크릿값";
		try {
			String text = URLEncoder.encode(title, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text + "&start=" + start; // json
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			return response.toString();
		} catch (Exception e) {
			log.debug(e+"");
		}
		return null;
	}
}
