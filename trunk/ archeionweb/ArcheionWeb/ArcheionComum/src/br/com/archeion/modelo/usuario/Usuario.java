package br.com.archeion.modelo.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.modelo.grupo.Grupo;

/**
 * Usuários do Sistema
 * 
 */

@Entity
@Table(name = "TB_USUARIOS")
public class Usuario extends AbstractTO implements UserDetails {

	/**
	 * Serial Version UID do Usuário.
	 */
	@Transient
	private static final long serialVersionUID = 9057226988302044363L;

	/**
	 * Número de Identificação do Usuário.
	 */
	@Id
	@Column(name = "ID_USUARIO", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;

	/**
	 * Nome do usuário.
	 */
	@Column(name = "NM_USUARIO", nullable = false, length = 40)
	private String nome;

	/**
	 * Login do usuário.
	 */
	@Column(name = "NM_LOGIN", nullable = false, length = 12)
	private String login;
		
	/**
	 * Senha do usuário.
	 */
	@Column(name = "TE_SENHA", nullable = false, length = 32)
	private String senha;

	/**
	 * Confirmação da Senha do usuário.
	 * Campo é transiente, não é gravado no banco
	 */
	@Transient
	private String confirmacaoSenha;
	
	 /**
	  * Informa se o usuário está ativo.
	 */
	@Column(name = "CS_PODE_ALUGAR", nullable = false)
	private Integer podeAlugar = 0;
	
	/**
	  * Informa se o usuário está ativo.
	 */
	@Column(name = "CS_SITUACAO_USUARIO", nullable = false)
	private Integer ativado = 0;
	
	
	public boolean getAtivado() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}

	/**
	 * Grupos que este Usuário faz parte.
	 */
	@ManyToMany 
	@JoinTable( name = "TB_GRUPOS_USUARIOS",  
				joinColumns = @JoinColumn( name = "ID_USUARIO" , referencedColumnName = "ID_USUARIO" ),
				inverseJoinColumns = @JoinColumn( name = "ID_GRUPO", referencedColumnName = "ID_GRUPO" ))
	private List<Grupo> grupos;
	
	/**
	 * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
	 * @return boolean
	 */
	public boolean isCredentialsNonExpired() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
	 * @return boolean
	 */
	public boolean isEnabled() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
	 * @return boolean
	 */
	public boolean isAccountNonLocked() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#getUsername()
	 * @return String
	 */
	public String getUsername() {
		return login;
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#getPassword()
	 * @return String;
	 */
	public String getPassword() {
		return senha;
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
	 * @return GrantedAuthority[]
	 */
	public GrantedAuthority[] getAuthorities() {
		List<GrantedAuthority> autorizacoes = new ArrayList<GrantedAuthority>();
		for (Grupo grupo : this.getGrupos()) {
			for (Funcionalidade authority : grupo.getFuncionalidades()) {
				autorizacoes.add(new GrantedAuthorityImpl(authority.getNome()));
			}
		}
		GrantedAuthority[] permissoes = new GrantedAuthority[autorizacoes.size()];
		return autorizacoes.toArray(permissoes);
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
	 * @return boolean
	 */
	public boolean isAccountNonExpired() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}

	/**
	 * @return Recuperando o campo id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 * 			Setando o campo id          
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return Recuperando o campo login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            Setando o campo login
	 */
	public void setLogin(final String login) {
		this.login = login;
	}

	/**
	 * @return Recuperando o campo senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha
	 *            Setando o campo senha
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return Recuperando o campo nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 * 			Setando o campo nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return Recuperando o campo ativado
	 */
	public boolean isAtivado() {
		return ativado==1?Boolean.TRUE:Boolean.FALSE;
	}
	/**
	 * @param ativo
	 * 			Setando o campo ativado
	 */
	public void setAtivado(boolean atv) {
		ativado = atv?1:0;
	}

	/**
	 * @return Recuperando o campo grupos
	 */
	public List<Grupo> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos
	 *            setando o campo grupos
	 */
	public void setGrupos(final List<Grupo> grupos) {
		this.grupos = grupos;
	}

	/**
	 * 
	 * @return Recuperando o campo confirmação de senha
	 */
	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	/**
	 * @param confirmacaoSenha
	 * 				setando o campos confirmação de senha
	 */
	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	/** 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (ativado * 1231);
		result = prime * result + ((grupos == null) ? 0 : grupos.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Usuario other = (Usuario) obj;
		if (ativado != other.ativado)
			return false;
		if (grupos == null) {
			if (other.grupos != null)
				return false;
		} else if (!grupos.equals(other.grupos))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}

	public boolean isPodeAlugar() {
		return this.podeAlugar==1?Boolean.TRUE:Boolean.FALSE;
	}
	
	public boolean getPodeAlugar() {
		return this.podeAlugar==1?Boolean.TRUE:Boolean.FALSE;
	}

	public void setPodeAlugar(boolean podeAlugar) {
		this.podeAlugar = podeAlugar?1:0;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("USUARIO[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append("]");
		
		return buff.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Usuario novo = new Usuario();
		novo.setId(getId());
		novo.setAtivado(getAtivado());
		novo.setGrupos(getGrupos());
		novo.setLogin(getLogin());
		novo.setNome(getNome());
		novo.setSenha(getSenha());
		novo.setPodeAlugar(getPodeAlugar());
		
		return novo;
	}
	
	
}