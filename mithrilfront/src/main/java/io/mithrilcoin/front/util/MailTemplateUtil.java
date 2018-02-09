package io.mithrilcoin.front.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class MailTemplateUtil {

	@Autowired
	private ResourceLoader resourceLoader;

	private final String AUTH_REPLACE_LINK = "#yourAuthLink";
	// 인증용 메일 템플릿
	private String authEmailTemplate;

	private String defaultSender;

	private String authTitle = "[MithrilPlay] Confirm your MithrilPlay account, ";

	@PostConstruct
	public void init() {
		Resource resource = resourceLoader.getResource("classpath:/email_verify.html");
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			authEmailTemplate = sb.toString();
			defaultSender = "dev@mithrilcoin.io";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAuthMailBody(String authLink) {
		return authEmailTemplate.replace(AUTH_REPLACE_LINK, authLink);
	}

	public String getDefaultSender() {
		return defaultSender;
	}

	public void setDefaultSender(String defaultSender) {
		this.defaultSender = defaultSender;
	}

	public String getAuthTitle() {
		return authTitle;
	}

}
