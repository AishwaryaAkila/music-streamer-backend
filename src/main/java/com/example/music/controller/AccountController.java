package com.example.music.controller;






import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.music.config.JwtTokenUtil;
import com.example.music.dao.account.AccountDao;
import com.example.music.dao.account.impl.AccountDaoImpl;
import com.example.music.dao.role.RoleDao;
import com.example.music.dto.AccountCreateDto;
import com.example.music.dto.SongsDto;
import com.example.music.dto.VerifyCodeDto;
import com.example.music.model.Account;
import com.example.music.model.Favourites;
import com.example.music.model.History;
import com.example.music.model.JwtResponse;
import com.example.music.model.Role;
import com.example.music.model.Songs;
import com.example.music.repository.FavouritesRepository;
import com.example.music.repository.SongsRepository;
import com.example.music.service.account.AccountService;
import com.example.music.service.account_verify.VerifyAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AccountController {
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private VerifyAccountService verifyaccount;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	

	@Autowired
	private UserDetailsService UserDetailsService;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private SongsRepository songsRepository;
	@Autowired
	private FavouritesRepository favouritesRepository;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/upload_song")
	public int uplaodImage(@RequestParam("audiofile") MultipartFile file,@RequestParam("audioimage") MultipartFile image,@RequestParam("songmodel") String json ) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		SongsDto songsDto = mapper.readValue(json, SongsDto.class);

		System.out.println(file.getBytes().length);
		accountService.saveSong(file,image,songsDto);
		
		return 1;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_latest")
	public List<Object[]> getLatest() {
		
		return accountService.getLatest();
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_recent")
	public List<Object[]> getRecent() {
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String email=userDetails.getUsername();
		Account account = accountDao.findByEmail(email).get();
		
		return accountService.getRecent(account.getId());
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_all_history")
	public List<Object[]> getAllRecent() {
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String email=userDetails.getUsername();
		Account account = accountDao.findByEmail(email).get();
		
		return accountService.getAllRecent(account.getId());
		
	}
	

	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/sign-up")
	public String signUp(@RequestBody AccountCreateDto accountCreateDto) throws Exception {
		try {
			Account account=accountService.findByEmail(accountCreateDto.getEmail()).get();
			if(!account.isActive()) {
				account.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
				account.setUsername(accountCreateDto.getUsername());
				accountDao.update(account);
				accountService.sendemail(account);
				return account.getId().toString();   
			}
				
			
			
		}
		catch(Exception NoSuchElementException) {
			Account account = accountService.createMember(accountCreateDto);
//			accountCreateDto.setId(account.getId());
			return account.getId().toString();	
		}
		return "-1";
		

	}
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/resend_otp")
	public String resendOtp(@RequestBody AccountCreateDto accountCreateDto) throws Exception {
		
		Account account=accountService.findByEmail(accountCreateDto.getEmail()).get();
		
		accountService.sendemail(account);
		return account.getId().toString();
	}

	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/verify")
	public int verifyCodeAction(@RequestBody VerifyCodeDto verifyCodeDto) throws Exception {
		
		
		return accountService.verifyCode(verifyCodeDto);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/authenticate")
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody AccountCreateDto accountCreateDto) throws Exception{
		authenticate(accountCreateDto.getEmail(), accountCreateDto.getPassword());
		Account account = accountDao.findByEmail(accountCreateDto.getEmail()).get();
		Long role=account.getRole();
		
		final UserDetails userDetails = UserDetailsService
		.loadUserByUsername(accountCreateDto.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println(token);
		return ResponseEntity.ok(new JwtResponse(token,role));
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
		throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
		throw new Exception("INVALID_CREDENTIALS", e);
		}
		}
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/get_user")
	public String getUser(@RequestBody String email) throws Exception{
		
		return accountService.getUser(email);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/verify_pass_code")
	public int passCode(@RequestBody VerifyCodeDto verifyCodeDto) throws Exception{
		
		return accountService.verifyPassCode(verifyCodeDto);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/change_password")
	public int changePassword(@RequestBody AccountCreateDto accountCreateDto ) throws Exception{
		
		return accountService.changePassword(accountCreateDto);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/get_song")
	public List<Object[]> getSong(@RequestBody Long id) {
		System.out.println(id);
		return accountService.getSong(id);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_all_songs")
	public List<Object[]> getAllLatest() {
		
		return accountService.getAllLatest();
		
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/play_next")
	public List<Object[]> playNext(@RequestBody Long id) {
		
		return accountService.playNext(id);
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/play_prev")
	public List<Object[]> playPrev(@RequestBody Long id) {
		
		return accountService.playPrev(id);
		
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_all_favourites")
	public List<Object[]> getFav(){
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String email=userDetails.getUsername();
		Account account=accountDao.findByEmail(email).get();
		return accountService.getFavourites(account.getId());
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_category")
	public List<Object[]> getCategory(@RequestParam("search") String key){
		return accountService.getCategory(key);
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_language")
	public List<Object[]> getLanguage(@RequestParam("search") String key){
		return accountService.getLanguage(key);
		
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/get_search")
	public List<Object[]> Search(@RequestParam("search") String key){
		System.out.println(key);
		return accountService.Search(key);
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/favourites")
	public List<Long> getFavo(){
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String email=userDetails.getUsername();
		Account account=accountDao.findByEmail(email).get();
		return accountService.getFavo1(account.getId());
//		if(key2==null) {
//			
//			return accountService.getFavo1(key1);
//			
//		}
//		else {
//			return accountService.getFavo2(key1, key2);
//			
//		}
		
		
		
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value="/add_fav")
	public int addFav(@RequestParam("email") String email,
            @RequestParam("song") String song1) {
		Long song=Long.parseLong(song1);
		Favourites fav=new Favourites();
		Account account=accountDao.findByEmail(email).get();
		Songs songs=songsRepository.findById(song).get();
		fav.setAccount(account);
		fav.setSongs(songs);
		favouritesRepository.save(fav);
		return 1;
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value="/remove_fav")
	public int removeFav(@RequestParam("email") String email,
            @RequestParam("song") String song1) {
		Long song=Long.parseLong(song1);
		Account account=accountDao.findByEmail(email).get();
		
		accountService.removeFav(account.getId(),song );
		return 1;
		
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value="/add_recent")
	public int addRecent(@RequestParam("email") String email,
            @RequestParam("song") String song1) {
		Long song=Long.parseLong(song1);
		History history=new History();
		Account account=accountDao.findByEmail(email).get();
		Long acc_id=account.getId();
		Songs songs=songsRepository.findById(song).get();
		accountService.getIdBySong(acc_id, song);
		history.setAccount(account);
		history.setSongs(songs);
		accountService.addRecent(history,acc_id);
		return 1;
		
	}
	
	
	
}
