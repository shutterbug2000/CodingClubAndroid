package engine_v01.assets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import static org.lwjgl.opengl.GL11.GL_FALSE;
import com.badlogic.gdx.graphics.GL20;
//import static org.lwjgl.opengl.GL20.*;

public class GLSLProgram {


	GL20 gl20;
	
	private int id;
	private Map<String, Integer> uniformLocations = new HashMap<String, Integer>();
	
	public GLSLProgram(int...shaders) {
		id = gl20.glCreateProgram();
		for(int shader : shaders) {
			gl20.glAttachShader(id, shader);
		}
		gl20.glLinkProgram(id);
	}
	
	public int loadShader(int type, String location) throws IOException {
		int id = gl20.glCreateShader(type);
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(location));
        String line;
        while ((line = reader.readLine()) != null) {
            shaderSource.append(line).append('\n');
        }
        reader.close();
        gl20.glShaderSource(id, shaderSource.toString());
        gl20.glCompileShader(id);
        //if(glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) throw new IOException(glGetShaderInfoLog(id, 1024));
        return id;
	}
	
	private int getUniformLocation(String name) {
		Integer location = uniformLocations.get(name);
		if(location == null) {
			location = gl20.glGetUniformLocation(id, name);
			uniformLocations.put(name, location);
		}
		return location;
	}
	
	public void use() {
		gl20.glUseProgram(id);
	}
	
	public void delete() {
		gl20.glDeleteProgram(id);
	}
	
	public void setUniform(String name, float value) {
		gl20.glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, float x, float y) {
		gl20.glUniform2f(getUniformLocation(name), x, y);
	}
	
	public void setUniform(String name, float x, float y, float z) {
		gl20.glUniform3f(getUniformLocation(name), x, y, z);
	}
	
	public void setUniform(String name, float r, float g, float b, float a) {
		gl20.glUniform4f(getUniformLocation(name), r, g, b, a);
	}
	
}
