package ua.foxminded.entity;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.ManyToOne;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NonNull
	@Column
	private String objectId;

	@NonNull
	@Column(name = "model")
	private String name;

	@NonNull
	@Column
	@Temporal(DATE)
	private Date year;

	@ManyToMany
	@JoinTable(inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"))
	private List<Category> category;

	@ManyToOne
	@JoinColumn(name = "maker_id", referencedColumnName = "id")
	private Maker maker;

}