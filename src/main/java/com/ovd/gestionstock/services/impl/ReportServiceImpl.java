package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.models.CommandeClient;
import com.ovd.gestionstock.repositories.ArticleRepository;
import com.ovd.gestionstock.repositories.ClientRepository;
import com.ovd.gestionstock.repositories.CommandeClientRepository;
import com.ovd.gestionstock.repositories.LigneCommandeClientRepository;
import com.ovd.gestionstock.services.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final CommandeClientRepository commandeClientRepository;

    private final ClientRepository clientRepository;

    private final ArticleRepository articleRepository;

    private final LigneCommandeClientRepository ligneClientRepository;
    @Override
    public String exportClientCommande(String reportFormat) throws FileNotFoundException, JRException {

        String path = "C:\\Users\\vincent.dione\\Desktop\\report";

        List<CommandeClient> commandeClients = commandeClientRepository.findAll();
        File file = ResourceUtils.getFile("classpath:facture/facture_client.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(commandeClients);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createdBy","Java Techie");
        JRDataSource dataSource1;
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if (reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path + "\\facture_clients.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path + "\\facture_clients.pdf");
        }
        return null;
    }
}
