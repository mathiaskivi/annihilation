/*******************************************************************************
 * Copyright 2014 stuntguy3000 (Luke Anderson) and coasterman10.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 ******************************************************************************/
package net.PlayFriik.Annihilation.maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class MapRollback {
	private final Logger log;
	private final File dataFolder;

	public MapRollback(Logger log, File dataFolder) {
		this.dataFolder = dataFolder;
		this.log = log;
	}

	public boolean loadMap(String name) {
		File mapsFolder = new File(dataFolder, "maps");
		if (!mapsFolder.exists()) {
			return false;
		}

		File levelSource = new File(dataFolder.getParentFile().getParentFile(), name);
		File regionSource = new File(dataFolder.getParentFile().getParentFile(), name + File.separator + "region");
		if (!levelSource.exists()) {
			return false;
		}
		if (!regionSource.exists()) {
			return false;
		}

		Bukkit.unloadWorld(name, false);

		File levelDestination = new File(mapsFolder, name);
		File regionDestination = new File(mapsFolder, name + "/region");
		try {
			copyFolder(levelSource, levelDestination);
			copyFolder(regionSource, regionDestination);
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Couldn't load map: {0}", name);
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveMap(String name) {
		File mapsFolder = new File(dataFolder, "maps");
		if (!mapsFolder.exists()) {
			mapsFolder.mkdir();
		}

		File levelSource = new File(dataFolder.getParentFile().getParentFile(), name);
		File regionSource = new File(dataFolder.getParentFile().getParentFile(), name + File.separator + "region");
		if (!levelSource.exists()) {
			return false;
		}
		if (!regionSource.exists()) {
			return false;
		}

		File levelDestination = new File(mapsFolder, name);
		File regionDestination = new File(mapsFolder, name + "/region");
		try {
			copyFolder(levelSource, levelDestination);
			copyFolder(regionSource, regionDestination);
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Couldn't save map: {0}", name);
			e.printStackTrace();
			return false;
		}
	}

	private void copyFolder(File src, File dest) throws IOException {
		if (!src.exists()) {
			log.log(Level.SEVERE, "File {0} does not exist, cannot copy!", src.toString());
			return;
		}
		if (src.isDirectory()) {
			boolean existed1 = dest.exists();
			if (!existed1) {
				dest.mkdir();
			}

			String[] srcFiles = src.list();

			if (existed1) {
				log.log(Level.INFO, "Copying folder {0} and overwriting {1}", new Object[]{src.getAbsolutePath(), dest.getAbsolutePath()});
			} else {
				log.log(Level.INFO, "Copying folder {0} to {1}", new Object[]{src.getAbsolutePath(), dest.getAbsolutePath()});
			}

			for (String file : srcFiles) {
				if (file.toString().startsWith("level") || file.toString().startsWith("r.")) {
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					copyFolder(srcFile, destFile);
				}
			}

			if (existed1) {
				log.log(Level.INFO, "Overwrote folder {0}", dest.getAbsolutePath());
			} else {
				log.log(Level.INFO, "Copied folder {0}", dest.getAbsolutePath());
			}
		} else {
			OutputStream out;
			boolean existed2;
			try (InputStream in = new FileInputStream(src)) {
				out = new FileOutputStream(dest);
				existed2 = dest.exists();
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			}
			out.close();

			if (existed2) {
				log.log(Level.INFO, "Overwrote file {0}", dest.getName());
			} else {
				log.log(Level.INFO, "Copied file {0}", dest.getName());
			}
		}
	}
}
