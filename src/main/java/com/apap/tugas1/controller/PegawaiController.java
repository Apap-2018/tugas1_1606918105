package com.apap.tugas1.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.apap.tugas1.service.*;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.model.*;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	@Autowired
	private JabatanService jabatanService;
	@Autowired
	private InstansiService instansiService;
	@Autowired
	private ProvinsiService provinsiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> get_allJabatan = jabatanService.find_allJabatan();
		model.addAttribute("listAllInstansi",instansiService.findAllInstansi());
		model.addAttribute("listJabatan",get_allJabatan);
		return"home";
	}
	
	/*
	 * Respon view berdasarkan nip
	 */
	 
	@RequestMapping(value = "/viewpegawai", method = RequestMethod.GET)
	private String  showPegawai(@RequestParam("nipPegawai") String nipPegawai, Model model) {
		PegawaiModel getPegawaiModel = pegawaiService.getPegawaiDetailByNip(nipPegawai).get();
		String nip = nipPegawai;
		model.addAttribute("pegawai",getPegawaiModel);
		model.addAttribute("nipPegawai",nip); 
		double gaji = 0;
		double gajiTerbesar = 0;
		        for (JabatanModel jabatanPegawai: getPegawaiModel.getJabatanList() ) {
		            if (jabatanPegawai.getGajiPokok() > gajiTerbesar) {
		                gajiTerbesar = jabatanPegawai.getGajiPokok();
		            }
		        }
		        gaji = gajiTerbesar;
		        double tunjangan = getPegawaiModel.getInstansi().getProvinsi().getPresentaseTunjangan();
		        gaji += (gaji * tunjangan/100);
		        Integer gajiInt = (int) gaji;
		model.addAttribute("gaji",gajiInt);
		
		return "view-pegawai";
	}
	/*Untuk Tambah Pegawai
	 * 
	 */
	
	@GetMapping("pegawai/add")
	private String tambahPegawai(Model model) {
		PegawaiModel newPegawai = new PegawaiModel();
		JabatanModel newJabatan = new JabatanModel();
		List<JabatanModel> jabatanPegawai = new ArrayList<JabatanModel>();
		jabatanPegawai.add(newJabatan);
		newPegawai.setJabatanList(jabatanPegawai);
	
	
		model.addAttribute("pegawai", newPegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
	
	
		return"formTambahPegawai";
		
	}
	/*
	 * Untuk tambah atau kurangi row pada Jabatan
	 */
	@PostMapping(value = "/pegawai/add", params= {"addRow"})
	public String addRowJabatan(@ModelAttribute PegawaiModel pegawai, BindingResult bindingResult, Model model) {
		if (pegawai.getJabatanList()== null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
		
		
		
		return "formTambahPegawai";
	}
	
	@PostMapping(value="/pegawai/add" , params= {"deleteRow"})
	public String deleteRowJabatan(@ModelAttribute PegawaiModel pegawai,BindingResult bindingResult, Model model, HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("deleteRow"));
	    pegawai.getJabatanList().remove(rowId.intValue());
	   
	    
	    model.addAttribute("pegawai", pegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
		
		return "formTambahPegawai";
		
	}
	
	/*
	 * Method ketika di submit
	 */
	
	 @PostMapping(value = "/pegawai/add", params= {"Submit"})
	 public String submitPegawai(@ModelAttribute PegawaiModel pegawai,Model model) {
		 String nip = "";
		 nip += pegawai.getInstansi().getId();
		 
		 String[] tglLahir = pegawai.getTanggalLahir().toString().split("-");
		 String tglLahirString = tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2, 4);
		 nip += tglLahirString;
		 
		 nip += pegawai.getTahunMasuk();
		 
		 int counterSama = 1;
			for (PegawaiModel pegawaiInstansi:pegawai.getInstansi().getPegawaiInstansi()) {
				if (pegawaiInstansi.getTahunMasuk().equals(pegawai.getTahunMasuk()) && pegawaiInstansi.getTanggalLahir().equals(pegawai.getTanggalLahir())) {
					counterSama += 1;
				}	
			}
			nip += "0" + counterSama;

			for (JabatanModel jabatan:pegawai.getJabatanList()) {
				System.out.println(jabatan.getNama());
			}

		pegawai.setNip(nip);
		 pegawaiService.addPegawai(pegawai);
		 
		 String message = "Pegawai bernama "+pegawai.getNama()+" dengan NIP: "+pegawai.getNip();
	     model.addAttribute("message", message);
	     return "addorupdate";
	 }
	
	/*
	 * Untuk mengatur form ubah pegawai 
	 */
	 @RequestMapping(value= "/instansi/get", method = RequestMethod.GET)
		public @ResponseBody List<InstansiModel> getInstansi(@RequestParam("provinsi") long id, Model model) {
	    	List<InstansiModel> allInstansi = instansiService.findAllInstansi();
	    	List<InstansiModel> instansiProvinsi = new ArrayList<InstansiModel>();
	    	for (InstansiModel i: allInstansi) {
	    		if (i.getProvinsi().getId()==id) {
	    			instansiProvinsi.add(i);
	    			System.out.println(i.getNama());
	    		}
	    	}
	    	System.out.println("ada objek sejumlah: "+instansiProvinsi.size());
	    	
			return instansiProvinsi;
		}
	
	@RequestMapping (value="/pegawai/ubah" , method=RequestMethod.GET)
		private String updatePegawai(@RequestParam("idPegawai") Long id, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiById(id);
		List<JabatanModel> jabatanPegawai = pegawai.getJabatanList();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
		
		model.addAttribute("dataupdatePegawai", new PegawaiModel());

		
		return "formUbahPegawai";
		}
	
	@PostMapping(value="/pegawai/ubah",params= {"submitUpdate"})
    public String submitUbahPegawai(@ModelAttribute PegawaiModel pegawai,Model model) {
		pegawaiService.updatePegawai(pegawai);
		pegawai=pegawaiService.getPegawaiById(pegawai.getId());
		String message = "Pegawai bernama "+pegawai.getNama()+" dengan NIP: "+pegawai.getNip();
        model.addAttribute("message", message);
        return "addorupdate";
    }
    public boolean adaJabatan(String id,PegawaiModel singlePegawai) {
    	for (JabatanModel jabatan: singlePegawai.getJabatanList()) {
    		if (jabatan.getId()==Integer.parseInt(id)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	/*
	 * Untuk tambah atau kurangi row pada Jabatan
	 */
	@PostMapping(value = "/pegawai/ubah", params= {"addRow"})
	public String tambahRowJabatan(@ModelAttribute PegawaiModel pegawai, BindingResult bindingResult, Model model) {
		if (pegawai.getJabatanList()== null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
		
		
		
		return "formTambahPegawai";
	}
	
	@PostMapping(value="/pegawai/ubah" , params= {"deleteRow"})
	public String hapusRowJabatan(@ModelAttribute PegawaiModel pegawai,BindingResult bindingResult, Model model, HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("deleteRow"));
	    pegawai.getJabatanList().remove(rowId.intValue());
	   
	    
	    model.addAttribute("pegawai", pegawai);
		model.addAttribute("allJabatan", jabatanService.find_allJabatan());
		model.addAttribute("provinsi", provinsiService.find_allProvinsi());
		model.addAttribute("instansi",instansiService.findAllInstansi());
		
		return "formTambahPegawai";
		
	}
	
	/*
	 * Untuk menampilkan halaman berupa pegawai tertua dan termuda
	 */
	@RequestMapping(value = "/pegawai/termuda-tertua", method=RequestMethod.GET)
	private String pegawaiInstansi(@RequestParam("idInstansi") long idInstansi, Model model) {
		PegawaiModel tertua = pegawaiService.findPegawaiTertua(idInstansi);
		PegawaiModel termuda= pegawaiService.findPegawaiTermuda(idInstansi);
		
		List<JabatanModel> jabatanPegawaiTertua = tertua.getJabatanList();
		Collections.sort(jabatanPegawaiTertua);
		Integer gajiTertua = (int) (jabatanPegawaiTertua.get(0).getGajiPokok() + 
				(jabatanPegawaiTertua.get(0).getGajiPokok()*
				(tertua.getInstansi().getProvinsi().getPresentaseTunjangan()/100)));
				
		List<JabatanModel> jabatanPegawaiTermuda = termuda.getJabatanList();
		Collections.sort(jabatanPegawaiTermuda);
		Integer gajiTermuda = (int)(jabatanPegawaiTermuda.get(0).getGajiPokok() 
		+ (jabatanPegawaiTermuda.get(0).getGajiPokok() *
		(termuda.getInstansi().getProvinsi().getPresentaseTunjangan()/100)));
		
		model.addAttribute("tertua", tertua);
		model.addAttribute("gajiTertua",gajiTertua);
		model.addAttribute("termuda", termuda);
		model.addAttribute("gajiTermuda",gajiTermuda);
		
		return "tabelTertuaTermuda";
	}
	
	//Fitur4 - Menampilkan Data Pegawai (Berdasarkan Instansi, Provinsi, Jabatan)
	@RequestMapping(value = "/pegawai/cari", method=RequestMethod.GET)
	private String cariPegawai(@RequestParam(value="idProvinsi",required=false) String idProvinsi,@RequestParam(value="idInstansi",required=false) String idInstansi,@RequestParam(value="idJabatan",required=false) String idJabatan,Model model) {
		model.addAttribute("listProvinsi", provinsiService.find_allProvinsi());
		model.addAttribute("listInstansi", instansiService.findAllInstansi());
		model.addAttribute("listJabatan", jabatanService.find_allJabatan());
		List<PegawaiModel> pegawai = pegawaiService.getAllPegawai();
		
		if ((idProvinsi==null || idProvinsi.equals("")) && (idInstansi==null||idInstansi.equals("")) && (idJabatan==null||idJabatan.equals(""))) {
		}
		else {
			if (idProvinsi!=null && !idProvinsi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					if (((Long)peg.getInstansi().getProvinsi().getId()).toString().equals(idProvinsi)) {
						temp.add(peg);
					}
				}
				pegawai = temp;
				model.addAttribute("idProvinsi", Long.parseLong(idProvinsi));
			}
			else {
				model.addAttribute("idProvinsi", "");
			}
			if (idInstansi!=null&&!idInstansi.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					if (((Long)peg.getInstansi().getId()).toString().equals(idInstansi)) {
						temp.add(peg);
					}
				}
				pegawai = temp;
				model.addAttribute("idInstansi", Long.parseLong(idInstansi));
			}
			else {
				model.addAttribute("idInstansi", "");
			}
			if (idJabatan!=null&&!idJabatan.equals("")) {
				List<PegawaiModel> temp = new ArrayList<PegawaiModel>();
				for (PegawaiModel peg: pegawai) {
					for (JabatanModel jabatan:peg.getJabatanList()) {
						if (((Long)jabatan.getId()).toString().equals(idJabatan)) {
							temp.add(peg);
							break;
						}
					}
					
				}
				pegawai = temp;
				model.addAttribute("idJabatan", Long.parseLong(idJabatan));
			}
			else {
				model.addAttribute("idJabatan", "");
			}
		}
		model.addAttribute("listPegawai",pegawai);
		return "cariPegawai";
	}
	
	
	
}



