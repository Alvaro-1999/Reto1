package com.gymapp.util;

import com.gymapp.model.Historico;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoricoXMLService {

    private final File xmlFile;

    public HistoricoXMLService() {
        this.xmlFile = new File("backups/historicos.xml");
        File dir = xmlFile.getParentFile();
        if (dir != null && !dir.exists()) dir.mkdirs();
    }

    /**
     * Guarda la lista de históricos en XML (sobrescribe).
     */
    public void saveHistoricoXML(List<Historico> historicos) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("historicos");
        doc.appendChild(root);

        for (Historico h : historicos) {
            Element item = doc.createElement("historico");

            // Atributos/elementos principales
            item.setAttribute("id", h.getId() == null ? "" : h.getId());
            item.setAttribute("workoutName", h.getWorkoutName() == null ? "" : h.getWorkoutName());
            item.setAttribute("date", h.getDate() == null ? "" : h.getDate());
            item.setAttribute("level", String.valueOf(h.getLevel()));
            item.setAttribute("estimatedTime", String.valueOf(h.getEstimatedTime()));
            item.setAttribute("completionProgress", String.valueOf(h.getCompletionProgress()));
            item.setAttribute("totalTime", String.valueOf(h.getTotalTime()));

            root.appendChild(item);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
    }

    /**
     * Lee el XML de historicos (si existe) y devuelve lista.
     */
    public List<Historico> loadHistoricoXML() throws Exception {
        List<Historico> result = new ArrayList<>();
        if (!xmlFile.exists()) return result;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList nodes = doc.getElementsByTagName("historico");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            Historico h = new Historico();
            h.setId(el.getAttribute("id"));
            h.setWorkoutName(el.getAttribute("workoutName"));
            h.setDate(el.getAttribute("date"));
            try { h.setLevel(Integer.parseInt(el.getAttribute("level"))); } catch (Exception e) { h.setLevel(0); }
            try { h.setEstimatedTime(Integer.parseInt(el.getAttribute("estimatedTime"))); } catch (Exception e) { h.setEstimatedTime(0); }
            try { h.setCompletionProgress(Integer.parseInt(el.getAttribute("completionProgress"))); } catch (Exception e) { h.setCompletionProgress(0); }
            try { h.setTotalTime(Integer.parseInt(el.getAttribute("totalTime"))); } catch (Exception e) { h.setTotalTime(0); }

            result.add(h);
        }

        return result;
    }

    public File getXmlFile() { return xmlFile; }
}
