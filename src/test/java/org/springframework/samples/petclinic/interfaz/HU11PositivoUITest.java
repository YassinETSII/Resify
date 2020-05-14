
package org.springframework.samples.petclinic.interfaz;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU11PositivoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	// El usuario manager accede a la lista de actividades, clica en el boton para
	// crear una nueva y rellena todos los campos con datos validos, por lo que se
	// registra la actividad y aparece el titulo de esta en la lista actualizada
	@Test
	public void testRegistrarActividadPos() throws Exception {
		driver.get("http://localhost:" + this.port);
		driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manager1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manager1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/actividades')]")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Añadir nueva actividad')]")).click();
		driver.findElement(By.id("titulo")).click();
		driver.findElement(By.id("titulo")).clear();
		driver.findElement(By.id("titulo")).sendKeys("Test Interfaz");
		driver.findElement(By.id("descripcion")).clear();
		driver.findElement(By.id("descripcion")).sendKeys("Test Interfaz");
		driver.findElement(By.id("fechaInicio")).click();
		driver.findElement(By.linkText("20")).click();
		driver.findElement(By.id("horaInicio")).click();
		driver.findElement(By.id("horaInicio")).clear();
		driver.findElement(By.id("horaInicio")).sendKeys("10:00");
		driver.findElement(By.id("horaFin")).clear();
		driver.findElement(By.id("horaFin")).sendKeys("13:00");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("Test Interfaz", driver.findElement(By.linkText("Test Interfaz")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
