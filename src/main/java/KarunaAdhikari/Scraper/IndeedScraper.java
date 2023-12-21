package KarunaAdhikari.Scraper;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class IndeedScraper{

	public static void main(String[] args) throws IOException, InterruptedException {
		// set up driver
		WebDriver driver = new ChromeDriver();

		// --user input--
		// scanner object
		Scanner scanner = new Scanner(System.in);

		// get job input
		String searchInput = "";
		System.out.println("What jobs are you looking for?: ");
		searchInput = scanner.nextLine();

		// --file name and date--
		// get current date
		Date currentDate = new Date();

		// define a date format for the file title
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// format the current date
		String formattedDate = dateFormat.format(currentDate);

		// create the file title
		String fileTitle = formattedDate + "-" + searchInput.replace(" ", "") + ".csv";

		// --create and setup file--
		// filewriter object
		FileWriter csvWriter = new FileWriter(fileTitle);

		// set up headings
		csvWriter.append("Title");
		csvWriter.append(",");
		csvWriter.append("Company");
		csvWriter.append(",");
		csvWriter.append("Location");
		csvWriter.append(",");
		csvWriter.append("Link");
		csvWriter.append("\n");

		// --driver setup--
		// navigate to the website
		driver.navigate().to("https://ca.indeed.com/?from=jobsearch-empty-whatwhere");
		driver.manage().window().maximize();

		// --run search--
		// type job into input
		driver.findElement(By.id("text-input-what")).sendKeys(searchInput);
		// driver.findElement(By.id("text-input-where")).sendKeys(locationInput);
		// locate and click the search button
		driver.findElement(By.className("yosegi-InlineWhatWhere-primaryButton")).click();

		// retrieve list of pagination objects -- all li elements in ul with
		// pagination-list class
		List<WebElement> pagination = driver.findElements(By.xpath("//ul[@class='css-1g90gv6 eu4oa1w0']/li"));

		// number of pages to traverse through
		// int numPages = pagination.size();
		// alt: first 5 pages
		int numPages = 5;

		// handle popup
		boolean popupClosed = false;
		

		// traverse through pages
		for (int i = 1; i <= numPages; i++) {
			System.out.println("Page: " + i);

			// get job titles (grab spans inside a elements for titles)
			List<WebElement> jobTitle = driver
					.findElements(By.xpath("//a[@class='jcs-JobTitle css-jspxzf eu4oa1w0']/span"));
			// count list size
			int numJobs = jobTitle.size();

			// get company name
			List<WebElement> jobCompany = driver.findElements(By.xpath("//span[@data-testid='company-name']"));

			// get company location
			List<WebElement> jobLocation = driver.findElements(By.xpath("//div[@data-testid='text-location']"));

			// get list of links
			// locate the <a> element based on h2 class
			List<WebElement> linkContainer = driver.findElements(By.xpath("//h2[contains(@class, 'jobTitle')]/a"));

			// get list of links
			List<String> jobLink = new ArrayList<String>();
			for (WebElement item : linkContainer) {
				jobLink.add(item.getAttribute("href").replace(",", " ").replace("\n", " "));
			}

			System.out.println("Num job titles, companies, locations, and links: " + numJobs + ", " + jobCompany.size()
					+ ", " + jobLocation.size() + ", " + jobLink.size());

			// traverse through list of jobs
			for (int j = 0; j < numJobs; j++) {
				Job jJob = new Job();

				// print out job title
				System.out.println("Job Title : " + jobTitle.get(j).getText());

				// set title for job object and replace , and "\n" to avoid csv issues
				jJob.setTitle(jobTitle.get(j).getText().replace(",", " ").replace("\n", " "));

				// print company name
				System.out.println("Job Company Name : " + jobCompany.get(j).getText());

				// set company for job object
				jJob.setCompany(jobCompany.get(j).getText().replace(",", " ").replace("\n", " "));

				// print location
				System.out.println("Job Location : " + jobLocation.get(j).getText());

				// set location for job object
				jJob.setLocation(jobLocation.get(j).getText().replace(",", " ").replace("\n", " "));

				// print link
				System.out.println("Job Link : " + jobLink.get(j));

				// separate
				System.out.println("---");

				// set link for job object
				jJob.setLink(jobLink.get(j));

				// add job details to csv file
				csvWriter.append(jJob.toString());
				csvWriter.append("\n");
			}

			// sleep
			Thread.sleep(1000);

			// find and click button to traverse to next page
			try {
				//find button
				WebElement pagei = driver.findElement(
						By.xpath("//ul[@class='css-1g90gv6 eu4oa1w0']/li/a[@data-testid='pagination-page-next']"));
				//click button
				pagei.click();
				
				//wait till pop up appears and closes
				while (!popupClosed) {
					try {
						// wait for popup
						Thread.sleep(1000);

						// click close button
						WebElement closeButton = driver.findElement(By.xpath("//button[@class='css-yi9ndv e8ju0x51']"));
						closeButton.click();

						// see bool to true
						popupClosed = true;
					} catch (Exception closeException) {
						// handle the exception if the close button is not found or clickable
						System.out.println("Popup not found.");
					}
				}
				
				// if not found
			} catch (Exception e) {
				System.out.println("Pagination button not found or not clickable.");
			}

		}

		// close the browser
		driver.quit();
		// close file writer
		csvWriter.close();
		// close scanner
		scanner.close();

	}
}