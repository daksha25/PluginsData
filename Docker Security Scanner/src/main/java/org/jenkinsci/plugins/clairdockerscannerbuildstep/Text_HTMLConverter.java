package org.jenkinsci.plugins.clairdockerscannerbuildstep;

import hudson.Launcher;
import hudson.EnvVars;
import hudson.Launcher.ProcStarter;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.util.ArgumentListBuilder;

//import org.apache.http.HttpResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

//import org.apache.http.HttpResponse;

import hudson.model.Computer;
import hudson.remoting.Callable;
import hudson.remoting.Channel;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.slaves.Channels;

import java.net.*;

/**
 * This class does the actual execution..
 * 
 */
public class Text_HTMLConverter {
	static int countHigh = 0;
	static int countMed = 0;
	static int countLow = 0;
	static int countNeg = 0;
	static int total;
	static int perHigh;
	static int perMed;
	static int perLow;
	static int perNeg;

	@SuppressWarnings("deprecation")
	public static FilePath text_to_html(File outFile, FilePath outfilFilePath1, PrintStream out, String build_no,
			FilePath target, AbstractBuild build, String jenkins_home, BuildListener listener, int buildNo)
					throws IOException, InterruptedException {

		File file = new File(outFile.toString());
		String content = new Scanner(new File(outFile.toString())).useDelimiter("\\Z").next();
		System.out.println("Build no is in TMTC " + build_no);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line = null;
		System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "");

		List<String> list = new ArrayList<String>();
		list.removeAll(Arrays.asList("", null));

		while ((line = bufferedReader.readLine()) != null) {
			list.add(line);
		}

		String[] stringArr = list.toArray(new String[0]);

		for (int j = 0; j < stringArr.length; j++) {
			stringArr[j] = stringArr[j].trim();
		}

		fileReader.close();

		String[] value = stringArr[0].split("Clair report for image ");
		String[] value1 = value[1].split(" ");

		out.println("<!doctype html>");

		out.println(
				"<div id=Heading style=\" font-family: Helvetica, Arial, sans-serif;font-size: 11px;text-decoration:underline;\"><h2 align=\"center\" style=\"color:Black;font-size: 12px;font-weight: bold;\"><b>Docker Security Report - "
						+ value1[0] + "</b></h2>");
		out.println("</div");
		out.println("<html lang = \"en\">");
		out.println("<head>");
		out.println("<meta charset = \"utf-8\">");
		out.println("<title>Clair Inputs</title>");
		out.println("<link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css\">");
		out.println("<link rel=\"stylesheet\" href=\"/resources/demos/style.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-1.12.4.js\"></script>");
		out.println("<script src=\"https://code.jquery.com/ui/1.12.1/jquery-ui.js\"></script>");
		out.println(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">");
		out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>");
		out.println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/vanilla_mint/style.css\">");
		out.println("<script type=\"text/javascript\" src=\"app/scripts/si-object-mint.js\"></script>");
		out.println("<script type=\"text/javascript\" language=\"javascript\">");

		// <![CDATA[
		out.println("SI.Mint.collapse     = true;"
				+ "           window.onload = function() { SI.Mint.staggerPaneLoading(true); SI.Mint.sizePanes(); SI.Mint.onloadScrolls(); };"
				+ "           window.onresize      = function() { SI.Mint.sizePanes(); };" + "           </script>");
		out.println(
				"<link href = \"https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css\"\" rel = \"stylesheet\">");
		out.println("<script src = \"https://code.jquery.com/jquery-1.10.2.js\"></script>");
		out.println("<script src = \"https://code.jquery.com/ui/1.10.4/jquery-ui.js\"></script>");
		out.println();
		out.println("<script>");
		out.println("$(function() {");
		out.println("$( \"#tabs-1\" ).tabs();");
		out.println("});");
		out.println("</script>");
		out.println("<script type=\"text/javascript\">");
		out.println("  var tablesToExcel = (function() {");
		out.println("    var uri = 'data:application/vnd.ms-excel;base64,'");
		out.println(
				"    , tmplWorkbookXML = '<?xml version=\"1.0\"?><?mso-application progid=\"Excel.Sheet\"?><Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">'");
		out.println(
				"      + '<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\"><Author>Axel Richter</Author><Created>{created}</Created></DocumentProperties>'");
		out.println("      + '<Styles>'");
		out.println("      + '<Style ss:ID=\"Currency\"><NumberFormat ss:Format=\"Currency\"></NumberFormat></Style>'");
		out.println("      + '<Style ss:ID=\"Date\"><NumberFormat ss:Format=\"Medium Date\"></NumberFormat></Style>'");
		out.println("     + '</Styles>' ");
		out.println("     + '{worksheets}</Workbook>'");
		out.println("    , tmplWorksheetXML = '<Worksheet ss:Name=\"{nameWS}\"><Table>{rows}</Table></Worksheet>'");
		out.println(
				"    , tmplCellXML = '<Cell{attributeStyleID}{attributeFormula}><Data ss:Type=\"{nameType}\">{data}</Data></Cell>'");
		out.println("   , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }");
		out.println("   , format = function(s, c) { return s.replace(/{(\\w+)}/g, function(m, p) { return c[p]; }) }");
		out.println("   return function(tables, wsnames, wbname, appname) {");
		out.println("     var ctx = \"\";");
		out.println("     var workbookXML = \"\";");
		out.println("     var worksheetsXML = \"\";");
		out.println("      var rowsXML = \"\";");
		out.println("      for (var i = 0; i < tables.length; i++) {");
		out.println("       if (!tables[i].nodeType) tables[i] = document.getElementById(tables[i]);");
		out.println("       for (var j = 0; j < tables[i].rows.length; j++) {");
		out.println("         rowsXML += '<Row>'");
		out.println("          for (var k = 0; k < tables[i].rows[j].cells.length; k++) {");
		out.println("            var dataType = tables[i].rows[j].cells[k].getAttribute(\"data-type\");");
		out.println("           var dataStyle = tables[i].rows[j].cells[k].getAttribute(\"data-style\");");
		out.println("          var dataValue = tables[i].rows[j].cells[k].getAttribute(\"data-value\");");
		out.println("           dataValue = (dataValue)?dataValue:tables[i].rows[j].cells[k].innerHTML;");
		out.println("           var dataFormula = tables[i].rows[j].cells[k].getAttribute(\"data-formula\");");
		out.println(
				"            dataFormula = (dataFormula)?dataFormula:(appname=='Calc' && dataType=='DateTime')?dataValue:null;");
		out.println(
				"            ctx = {  attributeStyleID: (dataStyle=='Currency' || dataStyle=='Date')?' ss:StyleID=\"'+dataStyle+'\"':''");
		out.println(
				"                  , nameType: (dataType=='Number' || dataType=='DateTime' || dataType=='Boolean' || dataType=='Error')?dataType:'String'");
		out.println("                   , data: (dataFormula)?'':dataValue");
		out.println("                  , attributeFormula: (dataFormula)?' ss:Formula=\"'+dataFormula+'\"':''");
		out.println("                 };");
		out.println("            rowsXML += format(tmplCellXML, ctx);");
		out.println("         }");
		out.println("         rowsXML += '</Row>'");
		out.println("        }");
		out.println("       ctx = {rows: rowsXML, nameWS: wsnames[i] || 'Sheet' + i};");
		out.println("       worksheetsXML += format(tmplWorksheetXML, ctx);");
		out.println("       rowsXML = \"\";");
		out.println("     }");
		out.println("      ctx = {created: (new Date()).getTime(), worksheets: worksheetsXML};");
		out.println("     workbookXML = format(tmplWorkbookXML, ctx);");
		out.println("console.log(workbookXML);");
		out.println("     var link = document.createElement(\"A\");");
		out.println("      link.href = uri + base64(workbookXML);");
		out.println("    link.download = wbname || 'Workbook.xls';");
		out.println("     link.target = '_blank';");
		out.println("      document.body.appendChild(link);");
		out.println("     link.click();");
		out.println("     document.body.removeChild(link);");
		out.println("   }");
		out.println("  })();");
		out.println("$( function() {			    $( \"#accordion\" ).accordion({"
				+ "			      collapsible: true,"
				+ "			            active: false,"
				+ "			            clearStyle: true"
				+ "			    });"
				+ "			  } );");
		out.println("  </script>");
		out.println("<style>");
		out.println("#tabs-1{font-size: 10px;font-weight:bold;}");
		out.println(".ui-widget-header {");
		out.println("background:Black;");
		// out.println("border: 1px solid #33AFFF;");
		out.println("color: #1fb4f7;");
		out.println("font-family: Helvetica, Arial, sans-serif;");
		out.println("}");
		out.println("</style>");
		out.println("<style>");

		out.println("table {");
		out.println("border=\"1|0\";border-color:Black;");
		out.println("width: 100%;height:50px;overflow:scroll;");

		out.println("font-family: Helvetica, Arial, sans-serif;");
		out.println("}");
		out.println("");
		out.println("th, td {");
		out.println("text-align: left;");
		out.println("padding: 8px;font-size:9px;");
		out.println("}");
		out.println("");
		out.println("tr:nth-child(even){background-color: #f2f2f2}");
		out.println("");
		out.println("th {");
		out.println("text-align: left;");
		out.println("background-color: White;");
		out.println("color: #1fb4f7; font-weight: bold;");
		out.println("}");
		out.println("</style>");
		out.println("<style>");
		out.println(
				".button { display: inline-block; padding: 10px 20px;font-size: 11px; cursor: pointer; text-align: center;text-decoration: none; outline: none;color: #fff;background-color: #1fb4f7;;"
						+ " border: none; border-radius: 14px; box-shadow: 0 5px #999; float: right; font-family: Helvetica, Arial, sans-serif; font-weight: bold; }");

		out.println(".button:hover {background-color: #1fb4f7;}"
				+ ".button:active { background-color: #1fb4f7;; box-shadow: 0 3px #666; transform: translateY(4px);}");
		/*
		 * out.println(".shadow {" +
		 * "                    -moz-box-shadow: inset 0 0 5px #888;" +
		 * "-webkit-box-shadow: inset 0 0 5px#888;" +
		 * "box-shadow: inner 0 0 5px #888;" + "               }");
		 */
		out.println(
				".scrollit {" + "                overflow:scroll;" + "               height:700px;" + "            }");
		out.println("body" + "            {" + "                  font-family: arial, helvetica, freesans, sans-serif;"
				+ "                  font-size: 100%;" + "                    color: #333;font-weight:bold"
				+ "                  background-color: #ddd;" + "             }");

		out.println(".box" + "           {" + "                  position: relative;"
				+ "                  width: 1000px;                    padding: 25px;"
				+ "                  margin: 0 auto;" + "                  background-color: #fff;"
				+ "                  -webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2), inset 0 0 50px rgba(0, 0, 0, 0.1);"
				+ "                  -moz-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2), inset 0 0 50px rgba(0, 0, 0, 0.1);"
				+ "                  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2), inset 0 0 50px rgba(0, 0, 0, 0.1);"
				+ "           }");

		out.println(".box:before, .box:after"
				+ "           {                    position: absolute;               width: 40%;                height: 10px;              content: ' ';              left: 12px;              bottom: 12px;"
				+ "                  background: transparent;                 -webkit-transform: skew(-5deg) rotate(-5deg);                  -moz-transform: skew(-5deg) rotate(-5deg);"
				+ "                  -ms-transform: skew(-5deg) rotate(-5deg);              -o-transform: skew(-5deg) rotate(-5deg);        transform: skew(-5deg) rotate(-5deg);                  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);"
				+ "                  -moz-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);               box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);                    z-index: -1;"
				+ "           } ");

		out.println(".box:after"
				+ "           {                    left: auto;                right: 12px;                     -webkit-transform: skew(5deg) rotate(5deg);                   -moz-transform: skew(5deg) rotate(5deg);"
				+ "                  -ms-transform: skew(5deg) rotate(5deg);                -o-transform: skew(5deg) rotate(5deg);"
				+ "                  transform: skew(5deg) rotate(5deg);" + "        } ");
		out.println("</style>");

		out.println("</head>");
		out.println("");

		String pattern_severity = "High|Medium|Low|Negligible";
		Pattern r = Pattern.compile(pattern_severity);

		String pattern_id = "(^[CVE-]+[0-9]{4}-[0-9]{4})";
		Pattern r1 = Pattern.compile(pattern_id);

		String desc = "(^[CVE]+[-].*)";
		Pattern strDesc = Pattern.compile(desc);

		String pattern_pack = "(^Package:.*)";
		Pattern r2 = Pattern.compile(pattern_pack);

		String pattern_link = "(^Link:.*)";
		Pattern r3 = Pattern.compile(pattern_link);

		String pattern_layer = "(^Layer:.*)";
		Pattern r4 = Pattern.compile(pattern_layer);

		Matcher m = null;
		Matcher m1 = null;
		Matcher m2 = null;
		Matcher m3 = null;
		Matcher m4 = null;
		Matcher d = null;

		int i = 1;

		String str_ID[] = new String[stringArr.length];
		String str_Pack[] = new String[stringArr.length];
		String str_Link[] = new String[stringArr.length];
		String str_Layer[] = new String[stringArr.length];
		String str_Severe[] = new String[stringArr.length];

		String str = null;

		while (i <= stringArr.length - 1) {
			m = r.matcher(stringArr[i]);
			m1 = r1.matcher(stringArr[i]);
			m2 = r2.matcher(stringArr[i]);
			m3 = r3.matcher(stringArr[i]);
			m4 = r4.matcher(stringArr[i]);
			d = strDesc.matcher(stringArr[i]);

			if (d.find()) {
				String strNew = d.group();
				content = content.replace(strNew, "<td>");
			}

			if (m1.find()) {
				str_ID[i] = m1.group();
			}

			if (m.find()) {
				str_Severe[i] = m.group();
			}

			if (m2.find()) {
				str = m2.group();
				content = content.replace(str, "</td>");
				str = str.replaceAll("Package: ", "");
				str_Pack[i] = str;
			}

			if (m3.find()) {
				str = m3.group();
				content = content.replace(stringArr[i], "");
				str = str.replaceAll("Link: ", "");
				str_Link[i] = str;
			}

			if (m4.find()) {
				str = m4.group();
				content = content.replace(str, "");
				str = str.replaceAll("Layer: ", "");
				str_Layer[i] = str;
			}
			i++;
		}

		String[] arr1 = content.split("<td>");

		String strNew = "";
		for (int j = 1; j < arr1.length; j++) {
			strNew = strNew.concat(arr1[j]);
		}

		String[] arr2 = strNew.split("</td>");

		String str_Desc[] = new String[str_ID.length];
		str_Desc = arr2;

		ArrayList<String> listID = new ArrayList<String>();
		for (String s : str_ID)
			if (s != null)
				listID.add(s);

		ArrayList<String> listSevere = new ArrayList<String>();
		for (String s : str_Severe)
			if (s != null)
				listSevere.add(s);

		ArrayList<String> listPack = new ArrayList<String>();
		for (String s : str_Pack)
			if (s != null)
				listPack.add(s);

		ArrayList<String> listLink = new ArrayList<String>();
		for (String s : str_Link)
			if (s != null)
				listLink.add(s);

		str_ID = listID.toArray(new String[listID.size()]);
		str_Severe = listSevere.toArray(new String[listSevere.size()]);
		str_Pack = listPack.toArray(new String[listPack.size()]);
		str_Link = listLink.toArray(new String[listLink.size()]);

		out.println("<body>");
		out.println("<div class=\"box\">");
		// out.println("<div id=\"test\" style=\"float:right;\">");
		out.println(
				"<button class=\"button\"; onclick=\"tablesToExcel(['tbl1','tbl2','tbl3','tbl4'], ['High','Medium','Low','Negligible'], 'Docker Security Report.xls', 'Excel')\">Export to Excel</button>");
		out.println(
				"<h4 style=\"color:#1fb4f7;font-family: Helvetica, Arial, sans-serif;font-size: 11px;font-weight: bold;\"><u><b>Severity Summary</b></u></h4>");
		out.println("<table style=\"width:30%; font-size: 11px;font-weight:bold\">");
		out.println("<tr style=\"font-weight:bold\">");
		out.println("<th>High</th>");
		out.println("<th>Medium</th>");
		out.println("<th>Low</th>");
		out.println("<th>Negligible</th>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><div  class=\"high-count\"></div></td>");
		out.println("<td><div  class=\"med-count\"></div></td>");
		out.println("<td><div  class=\"low-count\"></div></td>");
		out.println("<td><div  class=\"neg-count\"></div></td>");
		out.println("</tr>");
		out.println("");
		out.println("</table>");
		out.println("<br>");
		out.println("</table>");
		out.println("<br>");
		out.println(
				"<div id=\"curve_chart\" style=\"width: 425px; height: 200px; float:left;margin:auto;border: 2px solid black;box-sizing: border-box;\"/></div>");
		out.println(
				"<div  id=\"donutchart\" style=\"width: 425px;float:right; height: 200px;margin:auto;border: 2px solid black;box-sizing: border-box;\"></div>");
		out.println("<p></p>");
		out.println("<br><br><br><br>");
		out.println("<br><br><br><br>");

		out.println("<p></p>");
		out.println("<br><br><br><br>");
		out.println("<hr>");
		out.println(
				"<h2 style=\"color:#1fb4f7;font-family: Helvetica, Arial, sans-serif;font-size: 11px;font-weight: bold;\"><u><b>Severity Details</b></u></h2>");
		out.println(
				"<h4 style=\"font-size:11px;\">Click on each Severity level to know more about the Severity details specific to that level.</h4>");
		out.println("<div id=\"accordion\">");
		out.println("<h2 style=\"font-size:11px;\">High</h2>");
		out.println("<div id = \"tabs-2\" class=\"scrollit\">");
		out.println("<table id=\"tbl1\">");
		out.println("<tr>");
		out.println("<th align=\"center\">ID</th>");
		out.println("<th align=\"center\">Description</th>");
		out.println("<th align=\"center\">Package</th>");
		out.println("<th align=\"center\">Link</th>");
		out.println("</tr>");

		int j = 0;
		while (j < str_ID.length) {
			if (str_Severe[j].equals("High")) {

				out.println("<tr>");
				out.println("<td width=\"10%\">" + "<font color=\"#ff6600  \">" + str_ID[j] + "</font>" + "</td>");
				out.println("<td width=\"45%\">" + str_Desc[j] + "</td>");
				out.println("<td width=\"20%\">" + str_Pack[j] + "</td>");
				out.println("<td><a href=\"" + str_Link[j] + "\">" + str_Link[j] + "</a></td>");
				out.println("</tr>");

				countHigh++;
			}
			j++;
		}

		out.println("</table>");
		out.println("");
		out.println("</div>");
		out.println("<h2 style=\"font-size:11px;\">Medium</h2>");
		out.println("<div id = \"tabs-3\" class=\"scrollit\">");
		out.println("<table id=\"tbl2\">");
		out.println("<tr>");
		out.println("<th align=\"center\">ID</th>");
		out.println("<th align=\"center\">Description</th>");
		out.println("<th align=\"center\">Package</th>");
		out.println("<th align=\"center\">Link</th>");
		out.println("</tr>");

		j = 0;
		while (j < str_ID.length) {
			if (str_Severe[j].equals("Medium")) {

				out.println("<tr>");
				out.println("<td width=\"10%\">" + "<font color=\"#ff6600 \">" + str_ID[j] + "</font>" + "</td>");
				out.println("<td width=\"45%\">" + str_Desc[j] + "</td>");
				out.println("<td width=\"20%\">" + str_Pack[j] + "</td>");
				out.println("<td><a href=\"" + str_Link[j] + "\">" + str_Link[j] + "</a></td>");
				out.println("</tr>");

				countMed++;
			}
			j++;
		}
		out.println("</table>");
		out.println("</div>");
			out.println("<h2 style=\"font-size:11px;\">Low</h2>");
		out.println("<div id = \"tabs-4\" class=\"scrollit\">");

		out.println("<table id=\"tbl3\">");
		out.println("<tr>");
		out.println("<th>ID</th>");
		out.println("<th>Description</th>");
		out.println("<th>Package</th>");
		out.println("<th>Link</th>");
		out.println("</tr>");

		j = 0;
		while (j < str_ID.length) {
			if (str_Severe[j].equals("Low")) {

				out.println("<tr>");
			out.println("<td width=\"10%\">" + "<font color=\"#ff6600\">" + str_ID[j] + "</font>" + "</td>");
				out.println("<td width=\"45%\">" + str_Desc[j] + "</td>");
				out.println("<td width=\"20%\">" + str_Pack[j] + "</td>");
				out.println("<td><a href=\"" + str_Link[j] + "\">" + str_Link[j] + "</a></td>");
				out.println("</tr>");

				countLow++;
			}
			j++;
		}

		out.println("</table>");
		out.println("</div>");
		out.println("<h2 style=\"font-size:11px;\">Negligible</h2>");
		out.println("<div id = \"tabs-5\" class=\"scrollit\">");

		out.println("<table id=\"tbl4\">");
		out.println("<tr>");
		out.println("<th>ID</th>");
		out.println("<th>Description</th>");
		out.println("<th>Package</th>");
		out.println("<th>Link</th>");
		out.println("</tr>");

		j = 0;
		while (j < str_ID.length) {
			if (str_Severe[j].equals("Negligible")) {

				out.println("<tr>");
				out.println("<td width=\"10%\">" + "<font color=\"#ff6600\">" + str_ID[j] + "</font>" + "</td>");
				out.println("<td width=\"45%\">" + str_Desc[j] + "</td>");
				out.println("<td width=\"20%\">" + str_Pack[j] + "</td>");
				out.println("<td><a href=\"" + str_Link[j] + "\">" + str_Link[j] + "</a></td>");
				out.println("</tr>");

				countNeg++;
			}
			j++;
		}

		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("</body>");

		out.println("<script type=\"text/javascript\">");
		out.println("$('div.high-count').text('" + countHigh + "');");
		out.println("$('div.med-count').text('" + countMed + "');");
		out.println("$('div.low-count').text('" + countLow + "');");
		out.println("$('div.neg-count').text('" + countNeg + "');");
		out.println("</script>");

		// draw pie chart
		out.println("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>");
		out.println("<script type=\"text/javascript\">");
		out.println("google.charts.load(\"current\", {packages:[\"corechart\"]});"
				+ "google.charts.setOnLoadCallback(drawChart);");
		out.println("function drawChart() {var data = google.visualization.arrayToDataTable(["
				+ "              ['Type', 'Vulnarability'], ['High', " + countHigh + "], ['Medium', " + countMed + "],"
				+ "     ['Low'," + countLow + "],     ['Negligible'," + countNeg + "]    ]);"
				+ "   var options = {  pieHole: 0.3 };");

		out.println("  var chart = new google.visualization.PieChart(document.getElementById('donutchart'));");
		out.println("chart.draw(data, options); }");
		out.println("  </script>");
		out.println("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>");
		out.println("<script type=\"text/javascript\">");
		out.println("google.charts.load(\'current\', {\'packages\':[\'corechart\']});"
				+ "google.charts.setOnLoadCallback(drawChart);");
		// draw line chart
		Properties props = new Properties();
		File file1 = Values_File.valuesFile(countHigh, countMed, countLow, countNeg, build_no, build, jenkins_home,
				listener);
		FileInputStream fis = new FileInputStream(file1);
		System.out.println("file input stream is" + fis);
		props.load(fis);
		Set<Object> keys = props.keySet();
		// System.out.println("keys are "+keys);
		List<Object> listKeys1 = new ArrayList<Object>();
		int high = 0;
		int med = 0;
		int low = 0;
		int neg = 0;

		listKeys1.addAll(keys);
		// System.out.println("lisetkeys 1 is "+listKeys1);
		List<Integer> listKeys = new ArrayList<Integer>();
		for (Object object : listKeys1) {
			String obj = object.toString();
			Integer intobj = Integer.parseInt(obj);
			listKeys.add(intobj);
		}
		System.out.println("list key values are " + listKeys);
		Collections.sort(listKeys);
		System.out.println("sorted listkeys " + listKeys);
		int sizeOfLoop = keys.size();
		out.println(
				"function drawChart() { var data = google.visualization.arrayToDataTable([ [\'Build\', \'High\', \'Medium\',\'Low\', \'Negligible\'], ");
		//condition of 0 in build number
		
		if (buildNo == Integer.parseInt("0")) {
			if(listKeys.size()<1)
			{
				listener.getLogger().println("No data available to draw the Graph");
			}
			else if(listKeys.size()> 0 && listKeys.size()<5)
			{
			for (int z = 0; z < sizeOfLoop; z++) {
				String datavalue = props.getProperty(listKeys.get(z).toString()).trim();
				System.out.println(datavalue);
				int key = Integer.parseInt(listKeys.get(z).toString());
				String[] array = datavalue.split("\\,");
				System.out.println("Array is: " + array);
				for (int n = 0; n < 4; n++) {
					high = Integer.parseInt(array[0].trim());
					med = Integer.parseInt(array[1].trim());
					low = Integer.parseInt(array[2].trim());
					neg = Integer.parseInt(array[3].trim());

				}
				out.println("[" + key + "," + high + "," + med + "," + low + "," + neg + "],");
			}
			out.println(" ]);var options = {" + "title: 'Docker Security Severity Trend'," + " curveType: 'function',"
					+ "legend: { position: 'bottom' }};");

			out.println("var chart = new google.visualization.LineChart(document.getElementById('curve_chart'))");

			out.println("chart.draw(data, options); }");

			fis.close();
			out.println("</script>");
			}
			
			else
			{
			for (int k = (listKeys.size() - 5); k < (listKeys.size()); k++) {
				String datavalue = props.getProperty(((listKeys.get(k)).toString()).trim());
				System.out.println("value is " + datavalue);
				int key = Integer.parseInt(((listKeys.get(k)).toString()));
				System.out.println("Key is " + key);
				String[] array = datavalue.split("\\,");
				// System.out.println("Array is: "+array);
				for (int n = 0; n < 4; n++) {
					high = Integer.parseInt(array[0].trim());
					med = Integer.parseInt(array[1].trim());
					low = Integer.parseInt(array[2].trim());
					neg = Integer.parseInt(array[3].trim());
				}
				out.println("[" + key + "," + high + "," + med + "," + low + "," + neg + "],");
			}
			out.println(" ]);var options = {" + "title: 'Docker Security Severity Trend'," + " curveType: 'function',"
					+ "legend: { position: 'bottom' }};");

			out.println("var chart = new google.visualization.LineChart(document.getElementById('curve_chart'))");

			out.println("chart.draw(data, options); }");

			fis.close();
			out.println("</script>");

		
			}
		}
			
		else if (listKeys.size() >= buildNo) {
			System.out.println("inside if");
			for (int k = (listKeys.size() - buildNo); k < (listKeys.size()); k++) {
				String datavalue = props.getProperty(((listKeys.get(k)).toString()).trim());
				System.out.println("value is " + datavalue);
				int key = Integer.parseInt(((listKeys.get(k)).toString()));
				System.out.println("Key is " + key);
				String[] array = datavalue.split("\\,");
				// System.out.println("Array is: "+array);
				for (int n = 0; n < 4; n++) {
					high = Integer.parseInt(array[0].trim());
					med = Integer.parseInt(array[1].trim());
					low = Integer.parseInt(array[2].trim());
					neg = Integer.parseInt(array[3].trim());
				}
				out.println("[" + key + "," + high + "," + med + "," + low + "," + neg + "],");
			}
			out.println(" ]);var options = {" + "title: 'Docker Security Severity Trend'," + " curveType: 'function',"
					+ "legend: { position: 'bottom' }};");

			out.println("var chart = new google.visualization.LineChart(document.getElementById('curve_chart'))");

			out.println("chart.draw(data, options); }");

			fis.close();
			out.println("</script>");

		} 
		else {
			for (int z = 0; z < sizeOfLoop; z++) {
				String datavalue = props.getProperty(listKeys.get(z).toString()).trim();
				System.out.println(datavalue);
				int key = Integer.parseInt(listKeys.get(z).toString());
				String[] array = datavalue.split("\\,");
				System.out.println("Array is: " + array);
				for (int n = 0; n < 4; n++) {
					high = Integer.parseInt(array[0].trim());
					med = Integer.parseInt(array[1].trim());
					low = Integer.parseInt(array[2].trim());
					neg = Integer.parseInt(array[3].trim());

				}
				out.println("[" + key + "," + high + "," + med + "," + low + "," + neg + "],");
			}
			out.println(" ]);var options = {" + "title: 'Docker Security Severity Trend'," + " curveType: 'function',"
					+ "legend: { position: 'bottom' }};");

			out.println("var chart = new google.visualization.LineChart(document.getElementById('curve_chart'))");

			out.println("chart.draw(data, options); }");

			fis.close();
			out.println("</script>");

		}

		out.println("</html>");
		out.println("");
		out.println("");
		out.close();
		return outfilFilePath1;

	}

	public static boolean checkQualityGate(int high, int low, int medium, Boolean Severity, AbstractBuild build) {
		System.out.println(countHigh + "," + Text_HTMLConverter.countMed + "," + Text_HTMLConverter.countLow);
		boolean result = false;
		int c = 0;
		if (Severity != false) {
			if (countHigh > high || countLow > low || countMed > medium) {
				result = true;
			}

		}
		countHigh = 0;
		countLow = 0;
		countMed = 0;
		countNeg = 0;
		return result;
	}
}
