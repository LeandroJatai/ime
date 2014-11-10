package br.edu.ifg.ime.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.Suport;

public class ImeTest implements Runnable {

	private Thread th = null;
	private ArrayList<UnitTest> unitTests = null;

	static LdProject base = null;
	static LdProject master = null;

	static int minPlays = 1;
	static int minMisPorPlay = 1;
	static int minNivel = 1;
	static int minThreads = 1;

	static int maxPlays = 1;
	static int maxMisPorPlay = 1;
	static int maxNivel = 1;
	static int maxTimeTestInMinutes = 60;
	static int maxThreads = 1;

	static boolean fullTest = true;

	static boolean verbose = false;
	static int timeVerbose = 5000;

	static PrintStream fOutResults = null;

	static ArrayList<int[]> trabPendentes = new ArrayList<int[]>();
	static int maxGetTrabs = 0;

	static int countPlay = 1;
	static int countAct = 1;
	static int countMi = 1;

	static String lmiFiles = "";
	static String ldFiles = "";
	static String resultPath = "";	

	public ImeTest() {
	}

	public static void main(String args[]) throws Exception {

		DAOConnection.isActivated = false;
		HashMap<String, String> p = new HashMap<String, String>();

		for (int i = 0; i < args.length; i++) {

			if (!args[i].startsWith("-"))
				continue;

			if ( i < args.length-1 && !args[i+1].startsWith("-"))
				p.put(args[i], args[i+1]);
			else
				p.put(args[i], null);
		}

		URL url = null;
		InputStream in = null;		 

		if (p.containsKey("-h")) {

			System.out.print("-h                  // Imprime este guia de parâmetros.\n"
					+ "-i [arquivo]        // arquivo de entrada. Valor Padrão: arq básico que acompanha o jar.\n"
					+ "-o [pasta de saida] // pasta de saida. Valor Padrão: onde estiver o jar.\n"
					+ "-mP [valor]         // quantidade mínima de plays que um MI da simulação pode ter. Valor Padrão: 1\n"
					+ "-MP [valor]         // quantidade máxima de plays que um MI da simulação pode ter. Valor Padrão: 1\n"
					+ "-mM [valor]         // quantidade mínima de MIs por Play. Valor Padrão: 1\n"
					+ "-MM [valor]         // quantidade máxima de MIs por Play. Valor Padrão: 1\n"
					+ "-t [valor]          // Tempo em minutos que cada teste pode permanecer sendo executado. Valor Padrão: 60\n"
					+ "-T [valor]          // Quatidade máxima de Threads que podem ser exetuadas. \n"
					+ "                       Testes são independentes e podem ser executados vários ao mesmo tempo. Valor Padrão: 1\n"
					+ "-n [valor]          // Profundidade máxima na construção. Valor Padrão: 1\n"
					+ "-v [valor]          // Mostra o que está fazendo de tempo em tempo, unidade segundos. Valor Padrão: 5\n"
					+ "-a                  // Aleatorizar teste. Aleatorização controlada que garante que: \n"
					+ "                         1) o nível [-n] será atingido. \n"
					+ "                         2) Quantidades de Plays sempre estarão entre [-mP, -MP]. \n"
					+ "                         3) Quantidade de MIs por Plays sempre estarão entre [-mM, -MM].\n"
					+ "                         Valor Padrão: não usar este parâmetro faz com que o algoritmo excute sempre até o máximo\n"
					+ "");


			System.exit(0);
		}


		//--------------------------------------------------------------------------------------------------
		// -i [arquivo] //arquivo de entrada. Valor Padrão: arq básico que acompanha o jar.
		if (p.containsKey("-i")) {
			try {
				in = new FileInputStream(p.get("-i"));
			}
			catch (Exception e) {
				in = ImeTest.getInputStreamOfResource("/lmi-basico.zip");
			}
		}
		else {
			in = ImeTest.getInputStreamOfResource("/lmi-basico.zip");
		}


		//--------------------------------------------------------------------------------------------------
		// -o [pasta de saida] //pasta de saida. Valor Padrão: onde estiver o jar.
		if (p.containsKey("-o")) 
			resultPath = p.get("-o");

		if (resultPath == null || resultPath.length() == 0) {
			File f = new File("");
			resultPath = f.getAbsolutePath();
		}

		if (!resultPath.endsWith("/"))
			resultPath += "/";

		ImeTest.lmiFiles = resultPath+"lmiFiles";
		ImeTest.ldFiles = resultPath+"ldFiles";

		File fResult = new File(ImeTest.lmiFiles);
		fResult.mkdirs();
		fResult = new File(ImeTest.ldFiles);
		fResult.mkdirs();
		fResult = new File(ImeTest.resultPath+"results");
		fResult.mkdirs();

		//--------------------------------------------------------------------------------------------------
		// -mP [valor] // quantidade mínima de plays que um MI da simulação pode ter. Valor Padrão: 1
		if (p.containsKey("-mP")) {
			ImeTest.minPlays =  Integer.parseInt(p.get("-mP"));
		}

		//--------------------------------------------------------------------------------------------------
		// -MP [valor] // quantidade máxima de plays que um MI da simulação pode ter. Valor Padrão: 1
		if (p.containsKey("-MP")) {
			ImeTest.maxPlays =  Integer.parseInt(p.get("-MP"));
		}

		//--------------------------------------------------------------------------------------------------
		// -mM [valor] // quantidade mínima de MIs por Play. Valor Padrão: 1
		if (p.containsKey("-mM")) {
			ImeTest.minMisPorPlay =  Integer.parseInt(p.get("-mM"));
		}

		//--------------------------------------------------------------------------------------------------
		// -MM [valor] // quantidade máxima de MIs por Play. Valor Padrão: 1
		if (p.containsKey("-MM")) {
			ImeTest.maxMisPorPlay =  Integer.parseInt(p.get("-MM"));
		}		

		//--------------------------------------------------------------------------------------------------
		// -t [valor] // Tempo em minutos que cada teste pode permanecer sendo executado. Valor Padrão: 60
		if (p.containsKey("-t")) {
			ImeTest.maxTimeTestInMinutes =  Integer.parseInt(p.get("-t"));
		}


		//--------------------------------------------------------------------------------------------------
		// -T [valor] // Quatidade máxima de Threads que podem ser exetuadas. Testes são independentes e podem ser executados vários ao mesmo tempo. Valor Padrão: 1
		if (p.containsKey("-T")) {
			ImeTest.maxThreads =  Integer.parseInt(p.get("-T"));
		}


		//--------------------------------------------------------------------------------------------------
		// -n [valor] // Profundidade máxima na construção. Valor Padrão: 1
		if (p.containsKey("-n")) {
			ImeTest.maxNivel =  Integer.parseInt(p.get("-n"));
		}
		//--------------------------------------------------------------------------------------------------
		// -n [valor] // Profundidade máxima na construção. Valor Padrão: 1
		if (p.containsKey("-v")) {
			ImeTest.verbose = p.containsKey("-v");
			ImeTest.timeVerbose = Integer.parseInt(p.get("-v")) * 1000;
		}

		//--------------------------------------------------------------------------------------------------
		// -a [valor] // Aleatorizar teste. Aleatorização controlada que garante que: 1) o nível [-n] será atingido. 2) Quantidades de Plays sempre estarão entre [-mP, -MP]. 3) Quantidade de MIs por Plays sempre estarão entre [-mM, -MM]. Valor Padrão: 1
		ImeTest.fullTest = !p.containsKey("-a"); 

		ImeWorkspace w = new ImeWorkspace();
		LdProject ldep = w.importZIPStream(in);
		ldep.workspace = w;
		fResult = new File(ImeTest.resultPath+"results/results-"+Suport.dateTimeYYYYMMDDHHMMSS()+".csv");
		ImeTest.fOutResults = new PrintStream(fResult); 

		fOutResults.println("\"Prefixo\""+
				", "+"\"Como_Terminou\""+
				", "+"\"Tempo_Execução\""+
				", "+"\"Nível\""+
				", "+"\"Play por MI\""+
				", "+"\"Composição por Play\""+
				", "+"\"Qtd MI gerado\""+
				", "+"\"Qtd Play gerado\""+
				", "+"\"Qtd Act gerado\""+
				", "+"\"Total de Elementos\""+
				", \"Começou\"" +
				", \"Importou entrada\"" +
				", \"Gerou árvore LMI\"" +
				", \"Exportou LMI\"" +
				", \"Exportou LD\""

				);		




		ImeTest t = new ImeTest(); 
		t.start(ldep); 

	}

	public void start(LdProject ldep) {

		if (th != null && th.isAlive()) {

			for (UnitTest ut: unitTests)
				ut.interrupt();	
			unitTests.clear();

			th.interrupt();
		}

		countPlay = 1;
		countAct = 1;
		countMi = 1;
		//maxCountNivel = 1;
		base = ldep;

		th = new Thread(this);
		th.start();

	}

	@Override
	public void run() {

		unitTests = new ArrayList<UnitTest>();


		try {					
			for (int iMaxNivel = 1; iMaxNivel <= maxNivel; iMaxNivel++) {
				for (int iMaxPlays = minPlays; iMaxPlays <= maxPlays; iMaxPlays++) {
					for (int imaxMisPorPlay = minMisPorPlay; imaxMisPorPlay <= maxMisPorPlay; imaxMisPorPlay++) {

						int[] trab = {(int)(Math.pow(imaxMisPorPlay, 3) + Math.pow(iMaxPlays, 2) + iMaxNivel), iMaxNivel, iMaxPlays, imaxMisPorPlay};
						trabPendentes.add(trab);

					}	
				}
			}

			Collections.sort(trabPendentes, new Comparator<int[]>() {

				@Override
				public int compare(int[] o1, int[] o2) {

					if (o1[0] < o2[0])
						return -1;
					else if  (o1[0] == o2[0])
						return 0;
					else
						return 1;
				}

			});



			int[] trab = null;

			boolean flagComplex = false;
			while (!trabPendentes.isEmpty()) {

				while (unitTests.size() >= maxThreads) {
					Thread.sleep(ImeTest.timeVerbose);

					manageUnitTests();
					fOutResults.flush();
				}

				flagComplex = false;
				for (UnitTest ut: unitTests) {
					if (ut.runningForHowLong() > (maxTimeTestInMinutes*60*1000)/4) {
						flagComplex = true;
					}
				}

				if (maxThreads == 1)
					flagComplex = true;

				if (flagComplex) {
					trab = trabPendentes.remove(0);
				} 
				else  {
					trab = trabPendentes.remove((trabPendentes.size()/4));
				}

				UnitTest ut = new UnitTest(trab[1], trab[2], trab[3], ImeTest.resultPath);
				unitTests.add(ut);
				Thread.sleep(3000);
				ut.start(base);
			}


			while (unitTests.size() >0) {
				Thread.sleep(ImeTest.timeVerbose);

				manageUnitTests();
				fOutResults.flush();
			}	

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);

	}

	private void manageUnitTests() {
		UnitTest tempoEstourado;
		UnitTest encerrado;
		//Teste de tempo máximo de execução
		tempoEstourado = null;
		encerrado = null;
		for (UnitTest ut: unitTests) {

			if (ut.runningForHowLong() > maxTimeTestInMinutes*60*1000) {
				tempoEstourado = ut;
				break;
			}

			if (!ut.th.isAlive()) {
				encerrado = ut;
				break;
			}

			if (ImeTest.verbose)
				printResults(System.out, ut, "Trabalhando...");
		}

		if (tempoEstourado != null) {
			tempoEstourado.th.interrupt();
			unitTests.remove(tempoEstourado);
			printResults(fOutResults, tempoEstourado, "Te");
			return;
		}

		if (encerrado != null) {
			unitTests.remove(encerrado);
			printResults(fOutResults, encerrado, "Fim");

			return;
		}
	}

	private void printResults(PrintStream out, UnitTest ut, String prefixo) {

		boolean flagTrab = prefixo.startsWith("Trabalhando");

		out.println("\""+prefixo+"\""+
				", "+"\""+ (!flagTrab?(ut.flagConcluidoComSucesso?"Concluiu como esperado":"Interrompido"):"trab...")+"\""+
				", "+ut.runningForHowLong()+
				", "+ut.maxNivel+ 
				", "+ ut.maxPlays +
				", "+ ut.maxMisPorPlay + 
				", " + ut.countMi +
				", " + ut.countPlay +
				", " + ut.countAct +
				", "+ ut.getCountObjsWkspc()+
				", \""+Suport.dateToStrBR(ut.tempos[0])+" - "+Suport.dateToStrBROnlyTime(ut.tempos[0])+"\"" +
				", \""+Suport.dateToStrBR(ut.tempos[1])+" - "+Suport.dateToStrBROnlyTime(ut.tempos[1])+"\"" +
				", \""+Suport.dateToStrBR(ut.tempos[2])+" - "+Suport.dateToStrBROnlyTime(ut.tempos[2])+"\"" +
				", \""+Suport.dateToStrBR(ut.tempos[3])+" - "+Suport.dateToStrBROnlyTime(ut.tempos[3])+"\"" +
				", \""+Suport.dateToStrBR(ut.tempos[4])+" - "+Suport.dateToStrBROnlyTime(ut.tempos[4])+"\""

				);		

		/*System.out.println(prefixo+"Encerrado. "+ut.runningForHowLong()+"  Nivel: "+ut.maxNivel+ 
						"  maxPlays: "+ ut.maxPlays +
						"  maxMisPorPlay: "+ ut.maxMisPorPlay + 
						"  countMis: " + ut.countMi +
						"  countPlay: " + ut.countPlay +
						"  countAct: " + ut.countAct+
						"  N de composições: "+ (ut.getListLdPlayerProject().size()-1)+
						"  N de Elementos: "+ ut.getCountObjsWkspc());
		 */	}


	public static InputStream getInputStreamOfResource(String fonte) {

		if (!fonte.startsWith("/"))
			fonte = "/"+fonte;

		InputStream in = ImeTest.class.getClass().getResourceAsStream(fonte);
		return in;


	}


}
