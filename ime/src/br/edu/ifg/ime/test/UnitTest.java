package br.edu.ifg.ime.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Play;

import sun.awt.image.ByteArrayImageSource;
import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.PlaysController;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.LearningDesignRef;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class UnitTest extends ImeWorkspace implements Runnable {

	Thread th = null;
	LdProject base = null;
	LdProject masterTest = null;

	private GregorianCalendar dIni = null;
	private GregorianCalendar dFim = null;
	
	public GregorianCalendar tempos[] = null;
	
	
	int maxPlays = 1;
	int maxMisPorPlay = 1;
	int maxNivel = 1;

	int countPlay = 0;
	int countAct = 0;
	int countMi = 0;
	int maxCountNivel = 1;

	String pathResultados = "";

	boolean flagConcluidoComSucesso = false;

	private int nivel = 0;
	//	private int nivelTab = 1;

	public UnitTest(int maxNivel, int maxMisPorPlay, int maxPlays, String pathResultados) {
		this.maxNivel = maxNivel;
		this.maxPlays = maxPlays;
		this.maxMisPorPlay = maxMisPorPlay;
		this.pathResultados = pathResultados;
	}

	public long runningForHowLong() {

		if (dIni == null)
			return 0;

		if (dFim == null)	
			return (new GregorianCalendar()).getTimeInMillis() - dIni.getTimeInMillis();
		else 	
			return (dFim).getTimeInMillis() - dIni.getTimeInMillis();

	}


	public void start(LdProject ldep) {

		if (th != null && th.isAlive()) {
			th.interrupt();
		}

		countPlay = 0;
		countAct = 0;
		countMi = 0;
		maxCountNivel = 1;
		base = ldep;
		nivel = 0;

		th = new Thread(this);
		th.start();

	}

	@Override
	public void run() {
		dIni = new GregorianCalendar();
		//System.out.println("Início: "+Suport.dateToStrBR(dIni)+" - "+Suport.dateToStrBROnlyTime(dIni));

		tempos = new GregorianCalendar[5];
		tempos[0] = new GregorianCalendar(); //iniciou

		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			base.workspace.zipImscpWithoutResumeMi(bOut, base.getIdentifier());
			base = this.importZIPStream(new ByteArrayInputStream(bOut.toByteArray()));
			tempos[1] = new GregorianCalendar(); //importou entrada
		} catch (Exception e) {
			System.out.println("Erro: "+ e.getMessage());
			dFim = new GregorianCalendar();
			System.out.println("Erro FIM: "+Suport.dateToStrBR(dFim)+" - "+Suport.dateToStrBROnlyTime(dFim));
			return;
		}

		String strMaster = this.criarCopiaLdep(base);
		masterTest = this.getLdProjectByIdentifier(strMaster);
		masterTest.getLd().setTitle("M"+String.valueOf(++countMi));
		gerarArvore(masterTest);
		tempos[2] = new GregorianCalendar(); // gerou arvore LMI
		try {
			String suf = Suport.dateTimeYYYYMMDDHHMMSS();
			FileOutputStream fOut = new FileOutputStream(ImeTest.lmiFiles+File.separator+"MI-"+suf+"-"+maxNivel+".zip");
			this.zipImscpWithoutResumeMi(fOut, strMaster);

			tempos[3] = new GregorianCalendar(); //Exportou LMI
			//dIni = new GregorianCalendar();
			//System.out.println("Início Redução: "+Suport.dateToStrBR(dIni)+" - "+Suport.dateToStrBROnlyTime(dIni));
			fOut = new FileOutputStream(ImeTest.ldFiles+File.separator+"UOL-"+suf+"-"+maxNivel+".zip");

			LdProject ldep = this.getLdProjectByIdentifier(strMaster);
			ldep.getLd().setTitle(ldep.getLd().getTitle()+" (Copia LD).");

			this.removeLdProject(base.getIdentifier());
			this.reduzirElementosNaoDuplicaveis(ldep);

			this.reduzirElementosDuplicaveis(ldep);

			//out = new ByteArrayOutputStream();	
			//w.xmlBackupProject(out, strLdep);
			//in = new ByteArrayInputStream(bOut.toByteArray());
			//this.importXMLStream(in);

			this.zipImscpWithoutResumeMi(fOut, strMaster, "imscp", "imsld", "IMS_LD_Level_"+ldep.getLd().getLevel().toUpperCase()+".xsd");


			tempos[4] = new GregorianCalendar(); //terminou e exportou LD
			
			flagConcluidoComSucesso = true;

		} catch (Exception e) {
			flagConcluidoComSucesso = false;

		}


		//	for (String strLog: log)
		//		System.out.println(strLog);

		dFim = new GregorianCalendar();
		//System.out.println("Fim: "+Suport.dateToStrBR(dFim)+" - "+Suport.dateToStrBROnlyTime(dFim));
		//System.out.println("Início: "+Suport.dateToStrBR(dIni)+" - "+Suport.dateToStrBROnlyTime(dIni));

		th.interrupt();

	}

	private void gerarArvore(LdProject master2) {

		/*	String tab = "";
		while (tab.length() < nivelTab)
			tab += "|";
		System.out.println(tab+master2.getLd().getTitle());
		 */
		int mLocalPlays = maxPlays;
		int mLocalMisPorPlay = maxMisPorPlay;
		int mLocalNivel = maxNivel;

		if (!ImeTest.fullTest) {
			mLocalPlays = (int) (Math.random()*maxPlays);
			mLocalMisPorPlay = (int) (Math.random()*maxMisPorPlay);
			mLocalNivel = (int) (Math.random()*maxNivel);			
		}		

		mLocalMisPorPlay = (mLocalMisPorPlay < ImeTest.minMisPorPlay? ImeTest.minMisPorPlay :mLocalMisPorPlay);
		mLocalPlays = (mLocalPlays == ImeTest.minPlays? ImeTest.minPlays :mLocalPlays);
		mLocalNivel = (mLocalNivel == 0? 1 :mLocalNivel);


		List<Play> lPlays = PlaysController.getPlays(master2);


		while (lPlays.size() < mLocalPlays) {
			Play play = new Play();
			play.setIdentifier(this.newIdentifier(play, "play"));

			if (!ImeTest.fullTest)
				lPlays.add(  (int)(lPlays.size()*Math.random())  , play); // Aleatoria a posição em que o play entrará
			else 
				lPlays.add(  play  );

			play.setTitle(play.getIdentifier());
		}

		/*nivelTab++;
		tab = ""; 
		while (tab.length() < nivelTab)
			tab += "|";*/

		Play pRemove  = null;

		for (Play p: lPlays) {

			p.setTitle("P"+String.valueOf(++countPlay));

			/*	tab = ""; 
			while (tab.length() < nivelTab)
				tab += "|";
			System.out.println(tab+p.getTitle());*/

			List<Act> lActs = p.getActList();

			int nMis = 0;

			/*nivelTab++;
			tab = ""; 
			while (tab.length() < nivelTab)
				tab += "|";*/
			for (Act a: lActs) {

				if (a.getLearningDesignRef() != null) {
					nMis++;
					if (nivel < mLocalNivel) {
						nivel++;
						if (nivel < mLocalNivel) 
							gerarArvore(this.getLdProjectByIdentifierOfLD(LearningDesignUtils.getIdentifier(a.getLearningDesignRef().getRef())));
						nivel--;
					}
					else {

						if (!ImeTest.fullTest) {
							maxCountNivel++;

							if (maxCountNivel == maxNivel) {
								maxCountNivel = 1;
								maxNivel--;
							}
						}
					}
				}
				else { 
					a.setTitle("A"+String.valueOf(++countAct));
					//				System.out.println(tab+a.getTitle());
				}

			}
			while (nMis < mLocalMisPorPlay) {

				nMis++;
				if (nivel <= mLocalNivel) {
					Act act = new Act();
					act.setTitle("a"+String.valueOf(++countAct));


					act.setIdentifier(this.newIdentifier(act, "act"));

					if (!ImeTest.fullTest) {
						lActs.add(  (int)(lActs.size()*Math.random())  , act ); // Aleatoria a posição em que o MI entrará
					}
					else
					   lActs.add( act );

					String strCopia = this.criarCopiaLdep(base);
					LdProject copia = this.getLdProjectByIdentifier(strCopia);
					copia.getLd().setTitle("M"+String.valueOf(++countMi));

					LearningDesign ld = copia.getLd();
					LearningDesignRef ldRef = new LearningDesignRef();
					ldRef.setRef(ld);
					act.setLearningDesignRef(ldRef);
					master2.agregarLd(copia);
					this.referenciar(copia.getIdentifier(), act); 

					nivel++;
					if (nivel < mLocalNivel) 
						gerarArvore(copia);
					nivel--;
				}
				else {

					if (p.getActList().size() == 0) {
						pRemove = p;

					}

					if (!ImeTest.fullTest) {
						maxCountNivel++;

						if (maxCountNivel == maxNivel) {
							maxCountNivel = 1;
							maxNivel--;
						}
					}

					/*	tab = ""; 
					while (tab.length() < nivelTab)
						tab += "|";
					System.out.println(tab+act.getTitle());*/
				}
			}

			//		nivelTab--;
		}
		lPlays.remove(pRemove);
		if (pRemove != null)
			countPlay--;

		//	nivelTab--;
	}
	public void interrupt() {

		if (th != null && th.isAlive()) {
			th.interrupt();
		}
	}

}
