package com.ecommerce.user_service.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="users")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="id",nullable=false,updatable=false)
	private UUID uuid;
	
	@Column(name="email",nullable=false,unique=true)
	private String email;
	
	@ToString.Exclude
	@Column(name="password",nullable=false)
	private String password;
	
	@Column(name="full_name",nullable=false)
	private String fullName;

	@Column(name="role",nullable=false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@CreationTimestamp
	@Column(name="created_at",updatable=false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="updated_at",updatable=false)
	private LocalDateTime updatedAt;
	
	@Builder.Default
	@Column(name="is_active",nullable=false)
	private boolean isActive = true;

	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        // Return the user's role as a GrantedAuthority
	        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
	    }

	    @Override
	    public String getUsername() {
	        return email; //  using email as username
	    }

	    @Override
	    public boolean isAccountNonExpired() { return true; }

	    @Override
	    public boolean isAccountNonLocked() { return true; }

	    @Override
	    public boolean isCredentialsNonExpired() { return true; }

	    @Override
	    public boolean isEnabled() { return isActive; }

		@Override
		public String getPassword() {
			return password;
					
		}

}
