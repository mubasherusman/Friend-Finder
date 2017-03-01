package friendFinder.googleMaps;

import java.util.regex.Pattern;

public class errorCheck {
	errorCheck()
	{
		
	}
	public  boolean isSpace(final String testCode)
	{
		boolean flag=false;
	    if(testCode != null)
	    {
	        for(int i = 0; i < testCode.length(); i++)
	        {
	            if(Character.isWhitespace(testCode.charAt(i)))
	            {
	                flag= true;
	                break;
	            }
	        }
	    }
		return flag;
	}
	public boolean isNumeric(String str)  
    {  
         try  
            {  
                Double.parseDouble(str);  
            }  
            catch(NumberFormatException nfe)  
            {  
                return false;  
            }  
        return true;  
    }
	public boolean isSpecialChar(String toCheck)
	{
	        boolean isContainsSC = false;
	        if (toCheck != null && !toCheck.equals(""))
	        {
	                java.util.regex.Matcher m = Pattern.compile("(?=.*?[`~!@#$%^&*()\\-=+\\\\\\|\\[{\\]};:'\",<>/?]).*$").matcher(toCheck);
	                while (m.find())
	                {
	                        isContainsSC = true;
	                }
	        }
	        return isContainsSC;
	}
	//private  Pattern ALPHANUMERIC = Pattern.compile("[A-Za-z0-9]+");
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public boolean emailValidator(String str)
    {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if( str == null)
        { return true; }
        else
        {
        java.util.regex.Matcher m = pattern.matcher(str);
        return m.matches();}
    }

}
