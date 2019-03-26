import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Klasyfikacja {

	public static void main(String[] args) {
		String fileName = "sciezka do regul \\klasyfikacja.fcl";
		FIS fis = FIS.load(fileName, true);
		if (fis == null) {
			System.err.println("Nie moge zaladowc pliku: '" + fileName + "'");
			return;
		}
		String csvFile = "sciezka do danych\\movie_metadata.csv";
		String line = "";
		String cvsSplitBy = ",";
		int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {
				String budget = "";
				String score = "";
				String[] country = line.split(cvsSplitBy);

				budget = country[22];
				score = country[25];

				if (budget.isEmpty() || score.isEmpty() || !budget.matches("^[0-9]*$") || !score.matches("[-+]?[0-9]*\\.?[0-9]*")) {
					continue;
				}
				//ograniczam tylko dla 100 filmow zeby nie wykonywalo sie za dlugo
				if (i > 100) break;
				i++;


				// Pokazuje reguly
				FunctionBlock functionBlock = fis.getFunctionBlock(null);
				JFuzzyChart.get().chart(functionBlock);

				// Ustawia wejscia
				fis.setVariable("budget", Double.valueOf(budget));
				fis.setVariable("imdb_score", Double.valueOf(score));

				// Wylicza zbiory rozmyte
				fis.evaluate();

				// Ustawia wyjscia
				Variable klasyfikacja = functionBlock.getVariable("klasyfikacja");

				// Pokazuje wykres zmiennych wyjsciowych

				JFuzzyChart.get().chart(klasyfikacja, klasyfikacja.getDefuzzifier(), true);


				System.out.println(" Title= " + country[11] + " , budget=" + budget + " , imd score=" + score + " klasyfikacja:" + fis.getVariable("klasyfikacja").getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
