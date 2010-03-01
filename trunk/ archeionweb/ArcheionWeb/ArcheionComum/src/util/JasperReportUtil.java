package util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
 
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
 
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
 
/**
 * Classe utilit�ria que reune m�todos para gera��o de relat�rio JasperReport.
 * 
 * @author vinicius.souto
 * 
 */
public class JasperReportUtil {
 
    /**
     * M�todo para gerar uma sa�da em PDF, no qual ir� solicitar para o usu�rio
     * salvar o arquivo gerado.
     * 
     * @param reportPath
     *            String - caminho do relat�rio
     * @param parametros
     *            Map - mapa com os par�metros
     * @param beanList
     *            List - lista com os dados a serem impressos na listagem
     * @param nomeDoArquivo
     *            String - nome do arquivo a ser gerado erro do sistema
     */
    @SuppressWarnings("unchecked")
    public static void gerarPDF(String reportPath, Map parametros,
            List beanList, String nomeDoArquivo) {
        
        try {
             // recupera o contexto do JSF
            FacesContext context = FacesContext.getCurrentInstance();
 
            // recuperando o contexto da aplica��o web
            ExternalContext eContext = ((ExternalContext) FacesContext
                    .getCurrentInstance().getExternalContext());
            // recuperando o response da aplica��o
            HttpServletResponse response = (HttpServletResponse) eContext
                    .getResponse();
 
            // definindo o exportador, para PDF
            JRExporter exporter = new JRPdfExporter();
            ServletContext sContext = (ServletContext) eContext.getContext();
            InputStream is = new FileInputStream(sContext.getRealPath("/")
                    + reportPath);
            // RelatorioPDF.class.getResourceAsStream( reportPath );
 
            JasperPrint printer;
 
            if (beanList == null) {
 
                // gera o relat�rio em mem�ria
                printer = JasperFillManager.fillReport(
                // local que est� o arquivo do modelo do relat�rio
                        is,
                        // par�metros a serem enviados para o relat�rio
                        parametros);
 
            } else {
 
                // gera o relat�rio em mem�ria
                printer = JasperFillManager.fillReport(
                // local que est� o arquivo do modelo do relat�rio
                        is,
                        // par�metros a serem enviados para o relat�rio
                        parametros,
                        // lista de beans a serem impressos no relat�rio
                        new JRBeanCollectionDataSource(beanList));
 
            }
 
            // lista de bytes do arquivo carregado em mem�ria
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
 
            // seta o objeto 'baos' como parametro de streamming de saida no
            // objeto 'exporter'
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            // seta o objeto 'printer' como parametro de impressao no objeto
            // 'exporter'
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, printer);
            // seta a codifica��o dos caracteres como parametro de impressao no
            // objeto 'exporter'
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "ISO-8859-9");
            // exporta o relat�rio com a saida escolha em mem�ria (formato
            // Streamming)
            exporter.exportReport();
 
            // definindo o cabe�alho do retornos
            response.addHeader("Content-Type", "application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename="
                    + nomeDoArquivo + ".pdf");
 
            // informa o tamanho total do arquivo
            response.setContentLength(baos.size());
            // escreve o relat�rio na mem�ria do objeto 'response'
            response.getOutputStream().write(baos.toByteArray());
            // fechando o array de bytes
            response.getOutputStream().flush();
 
            // libera a mem�ria
            baos.flush();
            // fecha o arquivo em mem�ria
            baos.close();
 
            // indica para o contexto do JSF que o response est�
            // completo/finalizado
            context.responseComplete();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * M�todo para gerar uma sa�da em RTF, no qual ir� solicitar para o usu�rio
     * salvar o arquivo gerado.
     * 
     * @param reportPath
     *            String - caminho do relat�rio
     * @param parametros
     *            Map - mapa com os par�metros
     * @param beanList
     *            List - lista com os dados a serem impressos na listagem
     * @param nomeDoArquivo
     *            String - nome do arquivo a ser gerado erro do sistema
     */
    @SuppressWarnings("unchecked")
    public static void gerarRTF(String reportPath, Map parametros,
            List beanList, String nomeDoArquivo){
        
        try {
 
            // recupera o contexto do JSF
            FacesContext context = FacesContext.getCurrentInstance();
 
            // recuperando o contexto da aplica��o web
            ExternalContext eContext = ((ExternalContext) FacesContext
                    .getCurrentInstance().getExternalContext());
            // recuperando o response da aplica��o
            HttpServletResponse response = (HttpServletResponse) eContext
                    .getResponse();
 
            ServletContext sContext = (ServletContext) eContext.getContext();
            InputStream is = new FileInputStream(sContext.getRealPath("/")
                    + reportPath);
            // RelatorioPDF.class.getResourceAsStream( reportPath );
 
            JasperPrint printer;
 
            // gera o relat�rio em mem�ria
            printer = JasperFillManager.fillReport(
            // local que est� o arquivo do modelo do relat�rio
                    is,
                    // par�metros a serem enviados para o relat�rio
                    parametros,
                    // lista de beans a serem impressos no relat�rio
                    new JRBeanCollectionDataSource(beanList));
 
            // lista de bytes do arquivo carregado em mem�ria
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
 
            JRRtfExporter rtf = new JRRtfExporter();
            rtf.setParameter(JRExporterParameter.JASPER_PRINT, printer);
            rtf.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            rtf.exportReport();
 
            // definindo o cabe�alho do retornos
            response.addHeader("Content-Type", "application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename="
                    + nomeDoArquivo + ".rtf");
 
            // informa o tamanho total do arquivo
            response.setContentLength(baos.size());
            // escreve o relat�rio na mem�ria do objeto 'response'
            response.getOutputStream().write(baos.toByteArray());
            // fechando o array de bytes
            response.getOutputStream().flush();
 
            // libera a mem�ria
            baos.flush();
            // fecha o arquivo em mem�ria
            baos.close();
 
            // indica para o contexto do JSF que o response est�
            // completo/finalizado
            context.responseComplete();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * M�todo para gerar uma sa�da em XLS, no qual ir� solicitar para o usu�rio
     * salvar o arquivo gerado.
     * 
     * @param reportPath
     *            String - caminho do relat�rio
     * @param parametros
     *            Map - mapa com os par�metros
     * @param beanList
     *            List - lista com os dados a serem impressos na listagem
     * @param nomeDoArquivo
     *            String - nome do arquivo a ser gerado erro do sistema
     */
    @SuppressWarnings("unchecked")
    public static void gerarXLS(String reportPath, Map parametros,
            List beanList, String nomeDoArquivo)  {
        
        try {
 
            // recupera o contexto do JSF
            FacesContext context = FacesContext.getCurrentInstance();
 
            // recuperando o contexto da aplica��o web
            ExternalContext eContext = ((ExternalContext) FacesContext
                    .getCurrentInstance().getExternalContext());
            // recuperando o response da aplica��o
            HttpServletResponse response = (HttpServletResponse) eContext
                    .getResponse();
 
            ServletContext sContext = (ServletContext) eContext.getContext();
            InputStream is = new FileInputStream(sContext.getRealPath("/")
                    + reportPath);
            // RelatorioPDF.class.getResourceAsStream( reportPath );
 
            JasperPrint printer;
 
            // gera o relat�rio em mem�ria
            printer = JasperFillManager.fillReport(
            // local que est� o arquivo do modelo do relat�rio
                    is,
                    // par�metros a serem enviados para o relat�rio
                    parametros,
                    // lista de beans a serem impressos no relat�rio
                    new JRBeanCollectionDataSource(beanList));
             
 
            // lista de bytes do arquivo carregado em mem�ria
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
 
            // JRRtfExporter xls = new JRRtfExporter();
            JExcelApiExporter xls = new JExcelApiExporter();
            xls.setParameter(JExcelApiExporterParameter.JASPER_PRINT, printer);
            xls.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, baos);
            xls
                    .setParameter(
                            JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                            Boolean.TRUE);
            xls.setParameter(
                    JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                    Boolean.TRUE);
            xls.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET,
                    Boolean.TRUE);
            xls.exportReport();
 
            // definindo o cabe�alho do retornos
            response.addHeader("Content-Type", "application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment; filename="
                    + nomeDoArquivo + ".xls");
            // informa o tamanho total do arquivo
            response.setContentLength(baos.size());
            // escreve o relat�rio na mem�ria do objeto 'response'
            response.getOutputStream().write(baos.toByteArray());
            // fechando o array de bytes
            response.getOutputStream().flush();
 
            // libera a mem�ria
            baos.flush();
            // fecha o arquivo em mem�ria
            baos.close();
 
            // indica para o contexto do JSF que o response est�
            // completo/finalizado
            context.responseComplete();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
} 
