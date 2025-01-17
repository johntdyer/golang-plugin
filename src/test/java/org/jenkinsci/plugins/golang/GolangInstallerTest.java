package org.jenkinsci.plugins.golang;

import org.junit.Test;

import static org.jenkinsci.plugins.golang.GolangInstaller.GolangInstallable;
import static org.jenkinsci.plugins.golang.GolangInstaller.GolangRelease;
import static org.jenkinsci.plugins.golang.GolangInstaller.InstallationFailedException;
import static org.junit.Assert.assertEquals;

public class GolangInstallerTest {

    private static final GolangInstallable LINUX_32 = createPackage("linux", "386", null);
    private static final GolangInstallable LINUX_64 = createPackage("linux", "amd64", null);
    private static final GolangInstallable OS_X_10_6 = createPackage("darwin", "amd64", "10.6");
    private static final GolangInstallable OS_X_10_8 = createPackage("darwin", "amd64", "10.8");

    @Test(expected = InstallationFailedException.class)
    public void testUnsupportedOs() throws InstallationFailedException {
        // Given we have configured a release we want to install
        GolangRelease release = createReleaseInfo();

        // When we try to get the install package for an OS which is not supported
        GolangInstaller.getInstallCandidate(release, "Android", "armv7a", null);

        // Then an exception should be thrown
    }

    @Test
    public void testLatestPackageForOsXVersionReturned() throws InstallationFailedException {
        // Given we have configured a release we want to install
        GolangRelease release = createReleaseInfo();

        // When we try to get the install package for a much newer OS X version
        GolangInstallable pkg = GolangInstaller.getInstallCandidate(release, "Mac OS X", "x86_64", "10.11.12");

        // Then we should get the newest package which is older than our version
        assertEquals("Got unexpected package", OS_X_10_8, pkg);
    }

    @Test
    public void testEarlierPackageForOsXVersionReturned() throws InstallationFailedException {
        // Given we have configured a release we want to install
        GolangRelease release = createReleaseInfo();

        // When we try to get the install package for an older, but supported OS X version
        GolangInstallable pkg = GolangInstaller.getInstallCandidate(release, "Mac OS X", "x86_64", "10.7");

        // Then we should get the newest package which is older than our version
        assertEquals("Got unexpected package", OS_X_10_6, pkg);
    }

    @Test
    public void testExactMatchPackageForOsXVersionReturned() throws InstallationFailedException {
        // Given we have configured a release we want to install
        GolangRelease release = createReleaseInfo();

        // When we try to get a install package which has an exact match on OS X version
        GolangInstallable pkg = GolangInstaller.getInstallCandidate(release, "Mac OS X", "x86_64", "10.6");

        // Then we should get the package which matches the given version exactly
        assertEquals("Got unexpected package", OS_X_10_6, pkg);
    }

    @Test(expected = InstallationFailedException.class)
    public void testUnsupportedOsXVersion() throws InstallationFailedException {
        // Given we have configured a release we want to install
        GolangRelease release = createReleaseInfo();

        // When we try to get a install package which has an exact match on OS X version
        GolangInstallable pkg = GolangInstaller.getInstallCandidate(release, "Mac OS X", "x86_64", "10.5");

        // Then an exception should be thrown
    }

    private static GolangRelease createReleaseInfo() {
        GolangRelease release = new GolangRelease();
        release.variants = new GolangInstallable[] {
                LINUX_32,
                LINUX_64,
                OS_X_10_6,
                OS_X_10_8
        };
        return release;
    }

    private static GolangInstallable createPackage(String os, String arch, String osxVersion) {
        GolangInstallable pkg = new GolangInstallable();
        pkg.os = os;
        pkg.arch = arch;
        pkg.osxversion = osxVersion;
        return pkg;
    }

}