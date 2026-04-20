package com.fitapp.tools;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

import java.io.File;
import java.util.Collection;

public class JDependRunner {

    public static void main(String[] args) throws Exception {

        JDepend jdepend = new JDepend();

        PackageFilter filter = new PackageFilter() {

            public boolean filter(String packageName) {
                // Return true for packages to include
                return packageName != null &&
                        (packageName.startsWith("com.fitapp.model") ||
                        packageName.startsWith("com.fitapp.controller"));
            }
        };

        jdepend.addDirectory("target/classes");


        // Run analysis
        jdepend.analyze();

        // Print summary report
        @SuppressWarnings("unchecked")
        // telling compiler that I am aware that getPackages returns a raw Collection ,and I am casting it
        Collection<JavaPackage> packages = (Collection<JavaPackage>) jdepend.getPackages();

        System.out.println("---- JDepend Report ----");
        for (JavaPackage pkg : packages) {
            if (!pkg.getName().startsWith("com.fitapp")) {
                continue; // skip external packages
            }
            double distance = pkg.distance();
            int ce = pkg.efferentCoupling();
            int ca = pkg.afferentCoupling();

            System.out.printf("Package: %s, Classes: %d, Ca: %d, Ce: %d, I: %.2f, A: %.2f, D: %.2f%n",
                    pkg.getName(), pkg.getClassCount(), ca, ce, pkg.instability(), pkg.abstractness(), distance);


            // Only enforce distance check for model and controller packages
            boolean enforce = pkg.getName().startsWith("com.fitapp.model") ||
                    pkg.getName().startsWith("com.fitapp.controller");
            // Fail build if package is too unstable
            double maxDistance = 0.3;
            if (enforce && distance > maxDistance) {
                throw new RuntimeException(
                        String.format("Package %s is too far from main sequence (D=%.2f > %.2f)",
                                pkg.getName(), distance, maxDistance));
            }

        }

    }
}

