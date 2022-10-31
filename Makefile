
java = java -Xmx900m

default: edu.berkeley.sbp.jar

tibdoc: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.tib.TibDoc \
		tests/tibdoc.g \
		tests/bitstream.tib

java15: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.misc.Java15 \
		tests/java15.g \
		tests/java15.test

demo: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.misc.Demo \
		tests/demo.g \
		'(11+2*3)-44'

demo2: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.misc.Demo2

ast: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.misc.TestAstGenerator tests/math.g

regress:
	make boot
	rm edu.berkeley.sbp.jar
	make test

VERSION = ${shell cat VERSION}
releases_dir = /afs/research.cs.berkeley.edu/project/sbp/releases/
git_repo = /afs/research.cs.berkeley.edu/project/sbp/edu.berkeley.sbp.git/
release: edu.berkeley.sbp.jar
	echo == making release ${VERSION} ==============================
	echo
	git push ${git_repo}
	git archive --prefix=sbp-${VERSION} HEAD | gzip > \
	  ${releases_dir}/sbp-${VERSION}.tgz
	cp edu.berkeley.sbp.jar ${releases_dir}/sbp-${VERSION}.jar
	emacs -nw ~/wix/src/research.cs.berkeley.edu/sbp/index.wix
	cd ~/proj/wix; make

#-Dsbp.verbose=true
test: edu.berkeley.sbp.jar
	$(java) -cp $< edu.berkeley.sbp.misc.RegressionTests \
		src/edu/berkeley/sbp/meta/meta.g \
		tests/testcase.g \
		tests/regression.tc

profile: edu.berkeley.sbp.jar
	$(java) -agentpath:/Applications/YourKit.app/bin/mac/libyjpagent.jnilib -cp $< edu.berkeley.sbp.misc.RegressionTests \
	        -profile \
		src/edu/berkeley/sbp/meta/meta.g \
		tests/testcase.g \
		tests/regression.tc

boot: edu.berkeley.sbp.jar
	cd src; \
	$(java) -cp ../$< \
		edu.berkeley.sbp.meta.MetaGrammar \
		../src/edu/berkeley/sbp/meta/meta.g \
		edu.berkeley.sbp.meta.MetaGrammar

edu.berkeley.sbp.jar: $(shell find src -name \*.java -or -name \*.scala)
	rm -rf bin
	mkdir -p bin
	javac  -cp javax.servlet.jar -d bin -sourcepath src `find src -name \*.java`
	scalac -d bin -sourcepath src `find src -name \*.scala`
	echo 'Main-Class: edu.berkeley.sbp.misc.CommandLine' > bin/manifest
	cd bin; jar cmf manifest ../$@ .
#-Xlint:unchecked

javadoc:
	rm -rf doc/api
	mkdir -p doc/api
	javadoc \
		-linksource \
		-windowtitle "SBP: the Scannerless Boolean Parser" \
		-sourcepath src \
		-header "<b>SBP </b><br>v1.0" \
		-public \
		-notree \
		-noindex \
		-nonavbar \
		-stylesheetfile doc/javadoc.css \
		-noqualifier all \
		-d doc/api \
		edu.berkeley.sbp \
		edu.berkeley.sbp.meta \
		edu.berkeley.sbp.chr \
		edu.berkeley.sbp.misc \
		edu.berkeley.sbp.util

clean:
	rm -rf doc/api edu.berkeley.sbp.jar bin edu.berkeley.sbp.tar.gz
	rm -rf Makefile.bak *.hi *.o *.class *.jar Header_*.hs Class_*.hs *_JVM.hs InterfaceMyClass

