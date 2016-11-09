package com.cjon.bank.service;

import java.util.ArrayList;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.util.DBTemplate;

public class BankService {

	private DBTemplate template;
	private BankDAO dao;

	public DBTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DBTemplate template) {
		this.template = template;
	}

	public BankDAO getDao() {
		return dao;
	}

	public void setDao(BankDAO dao) {
		this.dao = dao;
	}

	public BankDTO deposit(BankDTO dto) {

		// BankDAO dao = new BankDAO(template);
		dao.setTemplate(template);
		
		dto = dao.update(dto);
		if(dto.isResult()) {
			template.commit();
		} else {
			template.rollback();
		}
		try {
			template.getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public BankDTO withdraw(BankDTO dto) {
		
		// BankDAO dao = new BankDAO(template);
		dao.setTemplate(template);
				
		dto = dao.updateWithdraw(dto);
		if(dto.isResult()) {
			template.commit();
		} else {
			template.rollback();
		}
		try {
			template.getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;		
	}

	public ArrayList<BankDTO> transfer(BankDTO dto1, BankDTO dto2) {

		// BankDAO dao = new BankDAO(template);
		dao.setTemplate(template);
				
		dto1 = dao.updateWithdraw(dto1);
		dto2 = dao.update(dto2);
		
		if(dto1.isResult() && dto2.isResult()) {
			template.commit();
		} else {
			template.rollback();
		}

		try {
			template.getCon().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();
		list.add(dto1);
		list.add(dto2);
		
		return list;
	}

}
