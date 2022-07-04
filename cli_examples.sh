# list all available locations. 
az account list-locations -o table 

# list all resource groups available
az group list -o table

# create storage account in the default location
az storage account create -n test12349876 -g BigDataAcademyJuly2022

# create storage account in the uksouth region
az storage account create -n test12349876eu -g BigDataAcademyJuly2022 -l uksouth

# same as the above with the full parameters names
az storage account create --name test12349876eu --resource-group BigDataAcademyJuly2022 --location uksouth

# list all accounts
az storage account list -g BigDataAcademyJuly2022 -o table

# delete the test12349876 account
az storage account delete -g BigDataAcademyJuly2022 -n test12349876

# show the connection string to the account
az storage account show-connection-string -n test12349876eu -g BigDataAcademyJuly2022
az storage account show-connection-string --query connectionString -n test12349876eu -g BigDataAcademyJuly2022
az storage account show-connection-string --query connectionString --name test12349876eu --resource-group BigDataAcademyJuly2022

# list all the containers for an account
az storage container list --account-name test12349876eu --connection-string "<connectionString>" -o table

# delete a container
az storage container delete --name blobcontainer --account-name test12349876eu --connection-string "<connectionString>"
