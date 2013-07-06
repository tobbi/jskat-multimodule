/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer, Markus J. Luzius and Daniel Loreck
 *
 * Version 0.13.0-SNAPSHOT
 * Copyright (C) 2013-05-10
 *
 * Licensed under the Apache License, Version 2.0. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jskat.data;

/**
 * Resolves the default save path for desktop applications
 */
public class DesktopSavePathResolver implements SavePathResolver {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultSavePath() {
		return System.getProperty("user.home") //$NON-NLS-1$
				+ System.getProperty("file.separator") + ".jskat" //$NON-NLS-1$ //$NON-NLS-2$
				+ System.getProperty("file.separator"); //$NON-NLS-1$
	}
}
