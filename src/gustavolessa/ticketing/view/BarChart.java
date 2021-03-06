package gustavolessa.ticketing.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BarChart extends JPanel {

	private double[] values;
	private String[] labels;
	private Color[] colors;
	private String title = "";

	public BarChart(double[] values, String[] labels, Color[] colors) {
		this.labels = labels;
		this.values = values;
		this.colors = colors;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (values == null || values.length == 0) {
			return;
		}

		double minValue = 0;
		double maxValue = 0;
		for (int i = 0; i < values.length; i++) {
			if (minValue > values[i]) {
				minValue = values[i];
			}
			if (maxValue < values[i]) {
				maxValue = values[i];
			}
		}

		Dimension dim = getSize();
		int panelWidth = dim.width;
		int panelHeight = dim.height;
		int barWidth = panelWidth / values.length;

		FontMetrics titleFontMetrics = g.getFontMetrics();//

		FontMetrics labelFontMetrics = g.getFontMetrics();//

		int titleWidth = titleFontMetrics.stringWidth(title);
		int stringHeight = titleFontMetrics.getAscent();
		int stringWidth = (panelWidth - titleWidth) / 2;
		g.drawString(title, stringWidth, stringHeight);

		int top = titleFontMetrics.getHeight();
		int bottom = labelFontMetrics.getHeight();
		if (maxValue == minValue) {
			return;
		}
		double scale = (panelHeight - top - bottom) / (maxValue - minValue);
		stringHeight = panelHeight - labelFontMetrics.getDescent();
		for (int j = 0; j < values.length; j++) {
			int valueP = j * barWidth + 1;
			int valueQ = top;
			int height = (int) (values[j] * scale);
			if (values[j] >= 0) {
				valueQ += (int) ((maxValue - values[j]) * scale);
			} else {
				valueQ += (int) (maxValue * scale);
				height = -height;
			}

			g.setColor(colors[j]);
			g.fillRect(valueP, valueQ, barWidth - 2, height);
			g.setColor(Color.black);
			g.drawRect(valueP, valueQ, barWidth - 2, height);

			int labelWidth = labelFontMetrics.stringWidth(labels[j]);
			stringWidth = j * barWidth + (barWidth - labelWidth) / 2;
			g.drawString(labels[j], stringWidth, stringHeight);
		}
	}
}