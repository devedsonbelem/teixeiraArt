package br.com.artes.teixeira.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "uploadimagemdb")
public class UploadImagemDB {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "tipo")
	private String tipo;
	@Lob
	private byte[] picByte;

	public UploadImagemDB() {
		super();
	}

	public UploadImagemDB(String name, String tipo, byte[] picByte) {
		this.name = name;
		this.tipo = tipo;
		this.picByte = picByte;
	}

	@Override
	public String toString() {
		return "UploadImagem [id=" + id + ", name=" + name + ", tipo=" + tipo + ", picByte=" + Arrays.toString(picByte)
				+ "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

}