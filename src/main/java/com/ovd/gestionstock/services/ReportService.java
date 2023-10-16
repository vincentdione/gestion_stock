package com.ovd.gestionstock.services;


import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ReportService {

    String exportClientCommande(String reportFormat) throws FileNotFoundException, JRException;

}
