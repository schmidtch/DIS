package at.kabeg.controller;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.Company;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class CompanyController {

	public ArrayList<Company> getCompanies() throws SQLException{
		ArrayList<Company> companies = new ArrayList<Company>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getCompanies.toString()));
		while (rs.next()){
			companies.add(new Company(Integer.parseInt(rs.getString("company_id")), rs.getString("name"), rs.getString("telefon"), rs.getString("email")));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return companies;
	}
	
	public void insertCompany(Company c) throws SQLException{

		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$name", c.getName());
		p.put("$telefon", c.getTelefon());
		p.put("$email", c.getEmail());
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertCompany.toString())));
		DBConnectionHandler.closeConnection(null, stmt);
		
	}
	
}
