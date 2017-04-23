/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.googlesource.gerrit.plugins.support.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Local path where a plugin can store its own private data.
 * <p>
 * A plugin or extension may receive this string by Guice injection to discover
 * a path where it can store configuration or other data that is private.
 * Differently from the Gerrit @PluginData annotation, this automatically
 * convert any underlying @PluginData annotation to a Path, regardless of
 * the version of Gerrit used.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@BindingAnnotation
public @interface PluginDataPath {
}
