package br.edu.ifpb.nutrif.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator implements NutrIFValidator {

	private Pattern pattern;
	private Pattern patternLetras;
	private Pattern patternPassword;
	private Matcher matcher;

	private static final String STRING_PATTERN = "[0-9a-zA-Z��������������������������������� ,/-�� ]*";
	private static final String STRING_PATTERN_SOMENTE_LETRAS = "[a-zA-Z��������������������������������� ]*";

	// Verifica se h�, ao menos:
	// - um n�mero ou caractere especial;
	// - uma letra min�scula;
	// - uma letra mai�scula.
	// O tamanho deve est� entre 8 e 25 caracteres.
	private static final String PASSWORD_PATTERN = "((?=.*[0-9a-zA-Z]).{6,25})";

	public StringValidator() {
		pattern = Pattern.compile(STRING_PATTERN);
		patternLetras = Pattern.compile(STRING_PATTERN_SOMENTE_LETRAS);
		patternPassword = Pattern.compile(PASSWORD_PATTERN);
	}

	@Override
	public boolean validate(final String value) {
		
		if (value == null || value.trim().equals(""))
			return false;
		
		matcher = pattern.matcher(value.trim());
		
		return matcher.matches();
	}
	
	public boolean validateSomenteLetras(final String value) {
		
		if (value == null || value.trim().equals(""))
			return false;
		
		matcher = patternLetras.matcher(value.trim());
		boolean t = matcher.matches();
		return matcher.matches();
	}

	public boolean validate(final String value, int tamanho) {
		
		boolean isValidate = validate(value);
		
		return (isValidate && value.length() <= tamanho);
	}

	public boolean validate(final String value, int tamanhoMenor,
			int tamanhoMaior) {
		
		return (value.length() >= tamanhoMenor && value.length() <= tamanhoMaior);
		
	}

	public boolean validate(String pattern, final String value,
			int tamanhoMenor, int tamanhoMaior) {
		
		return (validate(value) && (value.length() >= tamanhoMenor && value
				.length() <= tamanhoMaior));
	}

	public boolean validatePassword(final String password) {
		
		if (password == null || password.trim().equals(""))
			return false;
		
		matcher = patternPassword.matcher(password);
		
		return matcher.matches();
	}
}