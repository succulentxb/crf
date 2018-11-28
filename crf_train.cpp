#include <string>
#include <iostream>

using namespace std;

int main(int argc, char const *argv[])
{
    string train_file;
    string lables_file;
    string template_file;
    if (argc == 4) {
        train_file = argv[1];
        lables_file = argv[2];
        template_file = argv[3];
        
    }
    else {
        cerr << "error: wrong arguments." << endl;
        return 1;
    }
    return 0;
}
