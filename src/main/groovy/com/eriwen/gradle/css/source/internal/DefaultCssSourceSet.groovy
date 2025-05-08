package com.eriwen.gradle.css.source.internal

import com.eriwen.gradle.css.source.CssProcessingChain
import com.eriwen.gradle.css.source.CssSourceSet
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.model.ObjectFactory
import org.gradle.internal.reflect.Instantiator 

class DefaultCssSourceSet implements CssSourceSet {

    private final String name
    private final String displayName
    private final SourceDirectorySet css
    private final CssProcessingChain processing
    private final FileCollection processed

    DefaultCssSourceSet(String name, Project project, Instantiator instantiator, FileResolver fileResolver) {
        this.name = name
        this.displayName = name
        ObjectFactory objectFactory = project.getObjects();
        css = objectFactory.sourceDirectorySet(name, String.format("%s CSS source", displayName));
        this.processing = instantiator.newInstance(DefaultCssProcessingChain, project, this, instantiator)
        this.processed = project.files({ processing.empty ? css : processing.last().outputs.files })
    }

    String getName() {
        name
    }

    SourceDirectorySet getCss() {
        css
    }

    SourceDirectorySet css(Action<SourceDirectorySet> action) {
        action.execute(css)
        css
    }

    CssSourceSet configure(Closure closure) {
        closure.setDelegate(this)
        closure.setResolveStrategy(Closure.DELEGATE_FIRST)
        closure.call()
        return this
    }

    CssProcessingChain getProcessing() {
        processing
    }

    CssProcessingChain processing(Action<CssProcessingChain> action) {
        action.execute(processing)
        processing
    }

    FileCollection getProcessed() {
        processed
    }
}
