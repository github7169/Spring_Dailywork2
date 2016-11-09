package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.util.DBTemplate;

public class BankDAO {

	private DBTemplate template;	
	
	public BankDAO() {
	}

	public BankDAO(DBTemplate template) {
		this.template = template;
	}
	
	public DBTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DBTemplate template) {
		this.template = template;
	}

	public BankDTO update(BankDTO dto) {
		
		Connection con = template.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			// 3. PreparedStatement 생성
			String sql = "update bank set balance = balance + ? where userid = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, dto.getBalance());
			pstmt.setString(2, dto.getUserid());
			// 4. Query 실행
			int count = pstmt.executeUpdate();
			// 5. 결과 처리
			if( count ==1 ) {
				String sql1 = "select userid, balance from bank where userid = ?";
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, dto.getUserid());
				rs = pstmt1.executeQuery();
				if(rs.next()) {
					dto.setBalance(rs.getInt("balance"));
				}
				dto.setResult(true);
				try {
					rs.close();
					pstmt1.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				dto.setResult(false);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				pstmt.close();			
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		
		return dto;
	}

	public BankDTO updateWithdraw(BankDTO dto) {

		Connection con = template.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {					
			// Transaction의 시작
			// 3. PreparedStatement 생성
			String sql = "update bank set balance = balance - ? where userid = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, dto.getBalance());
			pstmt.setString(2, dto.getUserid());
			// 4. Query 실행
			int count = pstmt.executeUpdate();
			// 5. 결과 처리
			if( count ==1 ) {
				String sql1 = "select userid, balance from bank where userid = ?";
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, dto.getUserid());
				rs = pstmt1.executeQuery();
				if(rs.next()) {
					dto.setBalance(rs.getInt("balance"));
				}
				if( dto.getBalance() < 0){
					System.out.println("잔액이 부족하여 출금할 수 없습니다");
					dto.setResult(false);
				} else { 
					dto.setResult(true);
				}
				
				try {
					rs.close();
					pstmt1.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				pstmt.close();			
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		
		return dto;
	}

}
