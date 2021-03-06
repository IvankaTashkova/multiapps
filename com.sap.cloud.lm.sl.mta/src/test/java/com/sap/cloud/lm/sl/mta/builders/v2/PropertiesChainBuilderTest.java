package com.sap.cloud.lm.sl.mta.builders.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.Platform;
import com.sap.cloud.lm.sl.mta.model.Resource;

@RunWith(Parameterized.class)
public class PropertiesChainBuilderTest {

    protected final Tester tester = Tester.forClass(getClass());

    protected final String deploymentDescriptorLocation;
    protected final String platformLocation;
    protected final Expectation[] expectations;

    protected PropertiesChainBuilder builder;
    protected List<String> moduleNames;
    protected List<String> resourceNames;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All module and resource types are present in the platform:
            {
                "mtad-01.yaml", "platform-01.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.JSON, "module-chain-04.json"),
                    new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-04.json"),
                    new Expectation(Expectation.Type.JSON, "resource-chain-01.json"),
                },
            },
            // (1) No module and resource types in the platform:
            {
                "mtad-01.yaml", "platform-02.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.JSON, "module-chain-05.json"),
                    new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-05.json"),
                    new Expectation(Expectation.Type.JSON, "resource-chain-06.json"),
                },
            },
            // (2) Some module and resource types are present in the platform:
            {
                "mtad-01.yaml", "platform-03.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.JSON, "module-chain-06.json"),
                    new Expectation(Expectation.Type.JSON, "module-chain-without-dependencies-06.json"),
                    new Expectation(Expectation.Type.JSON, "resource-chain-03.json"),
                },
            },
// @formatter:on
        });
    }

    public PropertiesChainBuilderTest(String deploymentDescriptorLocation, String platformLocation, Expectation[] expectations) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformLocation = platformLocation;
        this.expectations = expectations;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();

        DeploymentDescriptor deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser,
                                                                                         getClass());
        resourceNames = getResourceNames(deploymentDescriptor.getResources());
        moduleNames = getModuleNames(deploymentDescriptor.getModules());

        Platform platform = null;
        if (platformLocation != null) {
            platform = MtaTestUtil.loadPlatform(platformLocation, getClass());
        }

        builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
    }

    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    private List<String> getResourceNames(List<Resource> resources) {
        List<String> resourceNames = new ArrayList<String>();
        for (Resource resource : resources) {
            resourceNames.add(resource.getName());
        }
        return resourceNames;
    }

    private List<String> getModuleNames(List<Module> modules) {
        List<String> moduleNames = new ArrayList<String>();
        for (Module module : modules) {
            moduleNames.add(module.getName());
        }
        return moduleNames;
    }

    protected PropertiesChainBuilder createPropertiesChainBuilder(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new PropertiesChainBuilder((DeploymentDescriptor) deploymentDescriptor, (Platform) platform);
    }

    @Test
    public void testBuildModuleChain() {
        tester.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> moduleChains = new ArrayList<List<Map<String, Object>>>();
                for (String moduleName : moduleNames) {
                    moduleChains.add(builder.buildModuleChain(moduleName));
                }
                return moduleChains;
            }
        }, expectations[0]);
    }

    @Test
    public void testBuildModuleChainWithoutDependencies() {
        tester.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> moduleChains = new ArrayList<List<Map<String, Object>>>();
                for (String moduleName : moduleNames) {
                    moduleChains.add(builder.buildModuleChainWithoutDependencies(moduleName));
                }
                return moduleChains;
            }
        }, expectations[1]);
    }

    @Test
    public void testBuildResourceChain() {
        tester.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> resourceChains = new ArrayList<List<Map<String, Object>>>();
                for (String resourceName : resourceNames) {
                    resourceChains.add(builder.buildResourceChain(resourceName));
                }
                return resourceChains;
            }
        }, expectations[2]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBuildResourceTypeChain() {
        for (String resourceName : resourceNames) {
            builder.buildResourceTypeChain(resourceName);
        }
    }

}
