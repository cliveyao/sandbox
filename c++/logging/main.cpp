#include "gtest/gtest.h"

int main(int argc, char** argv) {
//	// First initialization
//	BoostLogging::InitSetLogLevel();
//	// Next logging
//	BoostLogging::LogAMessageForAllLevels();
	::testing::InitGoogleTest(&argc, argv);
	// threadsafe works on linux; fast is alternative
	::testing::FLAGS_gtest_death_test_style = "threadsafe";
	return RUN_ALL_TESTS();
}

