/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.kotlin.backend.konan

import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.file.*

class TempFiles(val outputName: String, pathToTmpFiles: String?) {
    val temporaryDir = createDir(pathToTmpFiles)

    val nativeBinaryFile    by lazy { File(temporaryDir,"${outputName}.kt.bc") }
    val cAdapterHeader      by lazy { File("${outputName}_api.h") }
    val cAdapterDef         by lazy { File(temporaryDir,"${outputName}_symbols.def") }
    val cAdapterCpp         by lazy { createTempFile("api", ".cpp").deleteOnExit() }
    val cAdapterBitcode     by lazy { createTempFile("api", ".bc").deleteOnExit() }

    val nativeBinaryFileName    get() = nativeBinaryFile.absolutePath
    val cAdapterCppName         get() = cAdapterCpp.absolutePath
    val cAdapterBitcodeName     get() = cAdapterBitcode.absolutePath

    /**
     * If path is not provided then create tmp directory
     * that will be deleted on exit.
     * Use or create given directory otherwise
     */
    private fun createDir(path: String? = null): File = if (path == null) {
        createTempDir("konan_tmp").deleteOnExit()
    } else {
        if (File(path).isFile) {
            throw IllegalArgumentException("Given file is not a directory: $path")
        } else {
            File(path).apply {
                if (!this.exists) {
                    this.mkdirs()
                }
            }
        }
    }
}

