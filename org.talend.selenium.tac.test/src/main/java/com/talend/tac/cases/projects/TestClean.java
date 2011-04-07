package com.talend.tac.cases.projects;

import junit.framework.Assert;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.talend.tac.cases.Login;

public class TestClean extends Login{
	public void okDelete() {
//		this.waitForElementPresent("!!!menu.project.element!!!", 30);
//		selenium.click("!!!menu.project.element!!!");
//		this.waitForElementPresent("//div[@class='x-grid3-cell-inner x-grid3-col-label']", 60);
		selenium.refresh();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selenium.mouseDown("//div[@class='x-grid3-cell-inner x-grid3-col-label']");
		selenium.chooseOkOnNextConfirmation();
		
		System.out.println(selenium.getText("//div[@class='x-grid3-cell-inner x-grid3-col-label']"));
		
		selenium.click("idSubModuleDeleteButton");
		assert (selenium.getConfirmation()
				.matches(other.getString("delete.project.warning")));

	}
  @Test(groups={"cleanbefore"})
  //@Parameters({ "NumbersOfprojects" })
  public void deleteAll() {
	  selenium.setSpeed(MID_SPEED);
	  this.waitForElementPresent("!!!menu.project.element!!!", 30);
		selenium.click("!!!menu.project.element!!!");
	  for(int i = 0;; i++){
		if(selenium.isElementPresent("//div[@class='x-grid3-cell-inner x-grid3-col-label']")){
		okDelete();
		}
		else{
			break;
		}
	  }
	  selenium.setSpeed(MIN_SPEED);
  }
}
